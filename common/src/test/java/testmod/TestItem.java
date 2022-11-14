package testmod;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldingItem;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.ItemFilteredFluidContainer;
import earth.terrarium.botarium.api.fluid.ItemFluidContainer;
import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TestItem extends Item implements EnergyItem, FluidHoldingItem {
    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public EnergyContainer getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(1000000);
    }

    @Override
    public ItemFluidContainer getFluidContainer(ItemStack stack) {
        return new ItemFilteredFluidContainer(FluidHooks.buckets(4), 1, stack, (integer, fluidHolder) -> fluidHolder.getFluid() == Fluids.WATER);
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
