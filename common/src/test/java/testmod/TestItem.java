package testmod;

import earth.terrarium.botarium.api.energy.*;
import earth.terrarium.botarium.api.fluid.*;
import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

public class TestItem extends Item implements EnergyItem, FluidHoldingItem {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public StatefulEnergyContainer<ItemStack> getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(stack, 1000000);
    }

    @Override
    public ItemFluidContainer getFluidContainer(ItemStack stack) {
        return new ItemFilteredFluidContainer(FluidHooks.buckets(4), 1, stack, (integer, fluidHolder) -> fluidHolder.getFluid() == Fluids.WATER);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        PlatformFluidItemHandler itemFluidManager = FluidHooks.getItemFluidManager(stack);
        long oxygen = itemFluidManager.getFluidInTank(0).getFluidAmount();
        tooltip.add(Component.literal("Water: " + oxygen + "mb").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

        PlatformItemEnergyManager energyManager = EnergyHooks.getItemEnergyManager(stack);
        long energy = energyManager.getStoredEnergy();
        tooltip.add(Component.literal("Energy: " + energy + "FE").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
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
            if (fromFluidHolder == null) InteractionResultHolder.success(player.getMainHandItem());
            if (toFluidHolder == null) InteractionResultHolder.success(player.getMainHandItem());

            player.displayClientMessage(Component.literal("To: " + toFluidHolder.getFluidAmount()), true);

            if (FluidHooks.moveItemToItemFluid(from, to, FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(1) / 1000, fromFluidHolder.getCompound())) > 0) {
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
