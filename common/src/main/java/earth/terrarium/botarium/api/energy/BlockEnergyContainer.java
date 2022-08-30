package earth.terrarium.botarium.api.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class BlockEnergyContainer implements EnergyContainer {
    private final int energyCapacity;
    private int energy;

    public BlockEnergyContainer(int energyCapacity) {
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

    @Override
    public void setEnergy(long energy) {
        this.energy = (int) energy;
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
}
