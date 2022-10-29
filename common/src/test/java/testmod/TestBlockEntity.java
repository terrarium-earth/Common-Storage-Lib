package testmod;

import earth.terrarium.botarium.api.energy.SimpleUpdatingEnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.botarium.api.fluid.*;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.api.item.SerializableContainer;
import earth.terrarium.botarium.api.item.SimpleItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TestBlockEntity extends BlockEntity implements EnergyBlock, FluidHoldingBlock, ItemContainerBlock{
    public SimpleUpdatingFluidContainer fluidContainer;
    private SimpleItemContainer itemContainer;
    private SimpleUpdatingEnergyContainer energyContainer;

    public TestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public TestBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    @Override
    public final StatefulEnergyContainer getEnergyStorage() {
        return energyContainer == null ? this.energyContainer = new SimpleUpdatingEnergyContainer(this, 1000000) : this.energyContainer;
    }

    @Override
    public UpdatingFluidContainer getFluidContainer() {
        return fluidContainer == null ? this.fluidContainer = new SimpleUpdatingFluidContainer(this, FluidHooks.buckets(2), 1, (i, fluidHolder) -> true) : this.fluidContainer;
    }

    @Override
    public SerializableContainer getContainer() {
        return itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void update() {
        this.setChanged();
        if (level != null) level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public void tick() {}
}
