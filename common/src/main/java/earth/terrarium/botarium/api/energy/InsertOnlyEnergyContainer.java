package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Updatable;
import net.minecraft.util.Mth;

public class InsertOnlyEnergyContainer extends SimpleUpdatingEnergyContainer {
    public InsertOnlyEnergyContainer(Updatable entity, int energyCapacity) {
        super(entity, energyCapacity);
    }

    @Override
    public long maxExtract() {
        return 0;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long internalExtract(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if(simulate) return extracted;
        this.energy -= extracted;
        return extracted;
    }
}
