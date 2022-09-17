package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Serializable;
import net.minecraft.core.Direction;

public interface EnergyContainer extends Serializable {
    long insertEnergy(long maxAmount, boolean simulate);
    long extractEnergy(long maxAmount, boolean simulate);

    void setEnergy(long energy);
    long getStoredEnergy();
    long getMaxCapacity();
    long maxInsert();
    long maxExtract();

    boolean allowsInsertion();
    boolean allowsExtraction();

    default EnergyContainer getContainer(Direction direction) {
        return this;
    }
}
