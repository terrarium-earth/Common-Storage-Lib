package earth.terrarium.botarium.api;

import net.minecraft.nbt.CompoundTag;

public interface EnergyHoldable {
    long insertEnergy(long maxAmount);
    long extractEnergy(long maxAmount);

    void setEnergy(long energy);
    long getStoredEnergy();
    long getMaxCapacity();

    CompoundTag serialize(CompoundTag tag);
    void deseralize(CompoundTag tag);
}
