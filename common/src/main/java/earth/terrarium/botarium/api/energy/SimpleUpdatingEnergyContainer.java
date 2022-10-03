package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SimpleUpdatingEnergyContainer implements StatefulEnergyContainer {
    protected final long energyCapacity;
    protected final Updatable updatable;
    protected long energy;

    public SimpleUpdatingEnergyContainer(Updatable entity, long energyCapacity) {
        this.updatable = entity;
        this.energyCapacity = energyCapacity;
    }

    @Override public long insertEnergy(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if (simulate) return inserted;
        this.energy += inserted;
        return inserted;
    }

    @Override public long extractEnergy(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if (simulate) return extracted;
        this.energy -= extracted;
        return extracted;
    }

    public long internalInsert(long maxAmount, boolean simulate) {
        return insertEnergy(maxAmount, simulate);
    }

    public long internalExtract(long maxAmount, boolean simulate) {
        return extractEnergy(maxAmount, simulate);
    }

    @Override public void setEnergy(long energy) {
        this.energy = energy;
    }

    @Override public long getStoredEnergy() {
        return energy;
    }

    @Override public long getMaxCapacity() {
        return energyCapacity;
    }

    @Override public long maxInsert() {
        return 1024;
    }

    @Override public long maxExtract() {
        return 1024;
    }

    @Override public CompoundTag serialize(CompoundTag tag) {
        tag.putLong("Energy", this.getStoredEnergy());
        return tag;
    }

    @Override public void deserialize(CompoundTag tag) {
        setEnergy(tag.getLong("Energy"));
    }

    @Override public boolean allowsInsertion() {
        return true;
    }

    @Override public boolean allowsExtraction() {
        return true;
    }

    @Override public void update() {
        updatable.update();
    }

    @Override public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }
}
