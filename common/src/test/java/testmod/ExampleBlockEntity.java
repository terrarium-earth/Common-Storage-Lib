package testmod;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.BlockEnergyContainer;
import earth.terrarium.botarium.api.energy.UpdatingEnergyContainer;
import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.FluidHoldable;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.FilteredFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity implements EnergyBlock, FluidHoldable {
    public UpdatingEnergyContainer energyContainer;
    public FluidContainer fluidContainer;
    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public UpdatingEnergyContainer getEnergyStorage() {
        if(energyContainer == null) {
            this.energyContainer = new BlockEnergyContainer(this, 1000000);
        }
        return energyContainer;
    }

    @Override
    public FluidContainer getFluidContainer() {
        if(fluidContainer == null) {
            this.fluidContainer = new FilteredFluidContainer(this, FluidHooks.buckets(2), 1, (i, fluidHolder) -> true);
        }
        return fluidContainer;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }
}
