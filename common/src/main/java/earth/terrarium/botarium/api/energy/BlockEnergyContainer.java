package earth.terrarium.botarium.api.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEnergyContainer implements EnergyContainer {
    private final int energyCapacity;
    private final BlockEntity blockEntity;
    private int energy;

    public BlockEnergyContainer(BlockEntity entity, int energyCapacity) {
        this.blockEntity = entity;
        this.energyCapacity = energyCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if(simulate) return inserted;
        this.energy += inserted;
        this.update();
        return inserted;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if(simulate) return extracted;
        this.energy -= extracted;
        this.update();
        return extracted;
    }

    @Override
    public void setEnergy(long energy) {
        this.energy = (int) energy;
        this.update();
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
    public CompoundTag serialize(CompoundTag tag) {
        tag.putLong("Energy", this.getStoredEnergy());
        return tag;
    }

    @Override
    public void deseralize(CompoundTag tag) {
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
}
