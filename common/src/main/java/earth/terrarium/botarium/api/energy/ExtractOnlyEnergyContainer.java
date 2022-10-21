package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Updatable;
import net.minecraft.util.Mth;

public class ExtractOnlyEnergyContainer extends SimpleUpdatingEnergyContainer {
    public ExtractOnlyEnergyContainer(Updatable entity, int energyCapacity) {
        super(entity, energyCapacity);
    }

    @Override
    public long maxInsert() {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long internalInsert(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if(simulate) return inserted;
        this.energy += inserted;
        return inserted;
    }
}
