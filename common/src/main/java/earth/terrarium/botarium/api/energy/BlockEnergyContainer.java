package earth.terrarium.botarium.api.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEnergyContainer implements StatefulEnergyContainer {
    protected final int energyCapacity;
    protected final BlockEntity blockEntity;
    protected long energy;

    public BlockEnergyContainer(BlockEntity entity, int energyCapacity) {
        this.blockEntity = entity;
        this.energyCapacity = energyCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if(simulate) return inserted;
        this.energy += inserted;
        return inserted;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if(simulate) return extracted;
        this.energy -= extracted;
        return extracted;
    }

    public long internalInsert(long maxAmount, boolean simulate) {
        return insertEnergy(maxAmount, simulate);
    }

    public long internalExtract(long maxAmount, boolean simulate) {
        return extractEnergy(maxAmount, simulate);
    }

    @Override
    public void setEnergy(long energy) {
        this.energy = energy;
    }

    @Override
    public long getStoredEnergy() {
        return energy;
    }

    @Override
    public long getMaxCapacity() {
        return energyCapacity;
    }

    @Override
    public long maxInsert() {
        return 1024;
    }

    @Override
    public long maxExtract() {
        return 1024;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        tag.putLong("Energy", this.getStoredEnergy());
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        setEnergy(tag.getLong("Energy"));
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    public void update() {
        blockEntity.setChanged();
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new LongEnergySnapshot(this);
    }

    public static class LongEnergySnapshot implements EnergySnapshot {
        private final long energy;

        public LongEnergySnapshot(BlockEnergyContainer container) {
            this.energy = container.getStoredEnergy();
        }

        @Override
        public void loadSnapshot(EnergyContainer container) {
            container.setEnergy(energy);
        }
    }
}
