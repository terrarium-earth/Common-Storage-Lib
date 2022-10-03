package testmod;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldingItem;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.ItemFilteredFluidContainer;
import earth.terrarium.botarium.api.fluid.ItemFluidContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ExampleItem extends Item implements EnergyItem, FluidHoldingItem {
    public ExampleItem(Properties properties) {
        super(properties);
    }

    @Override public EnergyContainer getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(1000000);
    }

    @Override public ItemFluidContainer getFluidContainer(ItemStack stack) {
        return new ItemFilteredFluidContainer(FluidHooks.buckets(4), 1, stack, (integer, fluidHolder) -> fluidHolder.getFluid() == Fluids.WATER);
    }
}
