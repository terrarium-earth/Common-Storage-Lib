package testmod;

import earth.terrarium.botarium.api.energy.EnergyHoldable;
import earth.terrarium.botarium.api.energy.BlockEnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldable;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.FilteredFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity implements EnergyHoldable, FluidHoldable {
    public EnergyContainer energyContainer;
    public FluidContainer fluidContainer;
    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public EnergyContainer getEnergyStorage() {
        if(energyContainer == null) {
            this.energyContainer = new BlockEnergyContainer(1000000);
        }
        return energyContainer;
    }

    @Override
    public FluidContainer getFluidContainer() {
        if(fluidContainer == null) {
            this.fluidContainer = new FilteredFluidContainer(FluidHooks.buckets(2), 1, (i, fluidHolder) -> true);
        }
        return fluidContainer;
    }
}
