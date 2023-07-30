package testmod;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.PlatformItemEnergyManager;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.common.energy.util.EnergyHooks;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidItemHandler;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestNonInterfaceItem extends Item {
    public TestNonInterfaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        ItemStackHolder holder = new ItemStackHolder(stack);
        if (FluidApi.isFluidContainingItem(stack)) {
            ItemFluidContainer itemFluidManager = FluidApi.getItemFluidContainer(holder);
            long oxygen = itemFluidManager.getFluids().get(0).getFluidAmount();
            long oxygenCapacity = itemFluidManager.getTankCapacity(0);
            tooltip.add(Component.literal("Water: " + oxygen + "mb / " + oxygenCapacity + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        if (EnergyApi.isEnergyItem(stack)) {
            EnergyContainer energyManager = EnergyApi.getItemEnergyContainer(holder);
            long energy = energyManager.getStoredEnergy();
            long energyCapacity = energyManager.getMaxCapacity();
            tooltip.add(Component.literal("Energy: " + energy + "FE / " + energyCapacity + "FE").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }
    }

    /*
     * Tests fluid transfer between 2 test items. To use, fill 1 test item with water and put it in the mainhand.
     * Then put another empty test item in the offhand. then right click to transfer from the mainhand to the
     * offhand and print the offhand amount.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        try {
            ItemStackHolder from = new ItemStackHolder(player.getMainHandItem());
            ItemStackHolder to = new ItemStackHolder(player.getOffhandItem());

            var fromFluidHolder = FluidHooks.getItemFluidManager(from.getStack()).getFluidInTank(0);
            var toFluidHolder = FluidHooks.getItemFluidManager(to.getStack()).getFluidInTank(0);
            if (fromFluidHolder == null) return InteractionResultHolder.success(player.getMainHandItem());
            if (toFluidHolder == null) return InteractionResultHolder.success(player.getMainHandItem());

            player.displayClientMessage(Component.literal("To: " + toFluidHolder.getFluidAmount()), true);

            if (FluidHooks.moveItemToItemFluid(from, to, FluidHooks.newFluidHolder(BuiltInRegistries.FLUID.get(new ResourceLocation("ad_astra", "oxygen")), FluidHooks.buckets(1), fromFluidHolder.getCompound())) > 0) {
                if (from.isDirty()) player.setItemInHand(interactionHand, from.getStack());
                if (to.isDirty()) player.setItemSlot(EquipmentSlot.OFFHAND, to.getStack());
                level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1, 1);
                return InteractionResultHolder.consume(player.getMainHandItem());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return InteractionResultHolder.success(player.getMainHandItem());
    }
}
