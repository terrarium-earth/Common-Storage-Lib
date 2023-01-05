package earth.terrarium.botarium.common.energy.impl;

import earth.terrarium.botarium.common.menu.base.EnergyContainer;
import earth.terrarium.botarium.common.menu.base.EnergySnapshot;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public record WrappedBlockEnergyContainer(BlockEntity blockEntity, EnergyContainer container) implements EnergyContainer, Updatable<BlockEntity> {

    @Override
    public long insertEnergy(long energy, boolean simulate) {
        long insert = container.insertEnergy(energy, simulate);
        if (!simulate) update(blockEntity);
        return insert;
    }

    @Override
    public long extractEnergy(long energy, boolean simulate) {
        long extract = container.extractEnergy(energy, simulate);
        if (!simulate) update(blockEntity);
        return extract;
    }

    @Override
    public void setEnergy(long energy) {
        container.setEnergy(energy);
        update(blockEntity);
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
        update(blockEntity);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void update(BlockEntity object) {
        object.setChanged();
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
