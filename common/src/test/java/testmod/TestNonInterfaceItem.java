package testmod;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.PlatformItemEnergyManager;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidItemHandler;
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
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestNonInterfaceItem extends Item {
    public TestNonInterfaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (FluidHooks.isFluidContainingItem(stack)) {
            PlatformFluidItemHandler itemFluidManager = FluidHooks.getItemFluidManager(stack);
            long oxygen = itemFluidManager.getFluidInTank(0).getFluidAmount();
            long oxygenCapacity = itemFluidManager.getTankCapacity(0);
            tooltip.add(Component.literal("Water: " + oxygen + "mb / " + oxygenCapacity + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        if (EnergyApi.isEnergyItem(stack)) {
            ItemStackHolder itemStackHolder = new ItemStackHolder(stack);
            EnergyContainer energyManager = EnergyApi.getItemEnergyContainer(itemStackHolder);
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
        } else if (interactionHand == InteractionHand.MAIN_HAND) {
            try {
                ItemStackHolder from = new ItemStackHolder(player.getMainHandItem());
                ItemStackHolder to = new ItemStackHolder(player.getOffhandItem());

                PlatformFluidItemHandler itemFluidManager = FluidHooks.getItemFluidManager(from.getStack());

                if (player.isShiftKeyDown()) {
                    if (FluidApi.moveFluid(to, from, FluidHooks.newFluidHolder(BuiltInRegistries.FLUID.get(new ResourceLocation("minecraft", "water")), FluidHooks.buckets(1), null), false) > 0) {
                        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1, 1);
                        if (from.isDirty()) player.setItemInHand(interactionHand, from.getStack());
                        if (to.isDirty()) player.setItemSlot(EquipmentSlot.OFFHAND, to.getStack());
                        return InteractionResultHolder.consume(player.getMainHandItem());
                    }
                } else {
                    if (FluidApi.moveFluid(from, to, FluidHooks.newFluidHolder(BuiltInRegistries.FLUID.get(new ResourceLocation("minecraft", "water")), FluidHooks.buckets(1), null), false) > 0) {
                        if (from.isDirty()) player.setItemInHand(interactionHand, from.getStack());
                        if (to.isDirty()) player.setItemSlot(EquipmentSlot.OFFHAND, to.getStack());
                        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1, 1);
                        return InteractionResultHolder.consume(player.getMainHandItem());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return InteractionResultHolder.success(player.getMainHandItem());
    }
}
