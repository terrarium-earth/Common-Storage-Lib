package earth.terrarium.botarium.api.energy;

import net.minecraft.nbt.CompoundTag;

public interface EnergyContainer {
    long insertEnergy(long maxAmount, boolean simulate);
    long extractEnergy(long maxAmount, boolean simulate);

    void setEnergy(long energy);
    long getStoredEnergy();
    long getMaxCapacity();

    CompoundTag serialize(CompoundTag tag);
    void deseralize(CompoundTag tag);

    boolean allowsInsertion();
    boolean allowsExtraction();

    void 
}
