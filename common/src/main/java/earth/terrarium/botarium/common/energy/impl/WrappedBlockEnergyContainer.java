package earth.terrarium.botarium.common.energy.impl;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public record WrappedBlockEnergyContainer(BlockEntity blockEntity,
                                          EnergyContainer container) implements EnergyContainer, Updatable<BlockEntity> {

    @Override
    public long insertEnergy(long energy, boolean simulate) {
        return container.insertEnergy(energy, simulate);
    }

    @Override
    public long extractEnergy(long energy, boolean simulate) {
        return container.extractEnergy(energy, simulate);
    }

    @Override
    public long internalInsert(long amount, boolean simulate) {
        long inserted = container.internalInsert(amount, simulate);
        if (!simulate) update(blockEntity);
        return inserted;
    }

    @Override
    public long internalExtract(long amount, boolean simulate) {
        long l = container.internalExtract(amount, simulate);
        if (!simulate) update(blockEntity);
        return l;
    }

    @Override
    public void setEnergy(long energy) {
        container.setEnergy(energy);
    }

    @Override
    public long getStoredEnergy() {
        return container.getStoredEnergy();
    }

    @Override
    public long getMaxCapacity() {
        return container.getMaxCapacity();
    }

    @Override
    public long maxInsert() {
        return container.maxInsert();
    }

    @Override
    public long maxExtract() {
        return container.maxExtract();
    }

    @Override
    public boolean allowsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        container.deserialize(nbt);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void update(BlockEntity object) {
        object.setChanged();
        object.getLevel().sendBlockUpdated(object.getBlockPos(), object.getBlockState(), object.getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
