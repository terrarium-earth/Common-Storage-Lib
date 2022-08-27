package earth.terrarium.botarium.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class BlockEnergyContainer implements EnergyContainer {
    private int energy;
    private final int energyCapacity;

    public BlockEnergyContainer(int energyCapacity) {
        this.energyCapacity = energyCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        this.energy += maxAmount;
        return inserted;
    }

    @Override
    public long extractEnergy(long maxAmount) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        this.energy -= maxAmount;
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
}
