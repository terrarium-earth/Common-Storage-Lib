package testmod;

import earth.terrarium.botarium.api.energy.BlockEnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.api.fluid.*;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializbleContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity implements EnergyBlock, FluidHoldingBlock, ItemContainerBlock{
    public BlockFilteredFluidContainer fluidContainer;
    private SimpleItemContainer itemContainer;
    private BlockEnergyContainer energyContainer;

    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public final StatefulEnergyContainer getEnergyStorage() {
        return energyContainer == null ? this.energyContainer = new BlockEnergyContainer(this, 1000000) : this.energyContainer;
    }

    @Override
    public UpdatingFluidContainer getFluidContainer() {
        return fluidContainer == null ? this.fluidContainer = new BlockFilteredFluidContainer(this, FluidHooks.buckets(2), 1, (i, fluidHolder) -> true) : this.fluidContainer;
    }

    @Override
    public SerializbleContainer getContainer() {
        return itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }
}
