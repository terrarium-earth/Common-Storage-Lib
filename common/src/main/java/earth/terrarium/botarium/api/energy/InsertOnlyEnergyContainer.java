package earth.terrarium.botarium.api.energy;

import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InsertOnlyEnergyContainer extends BlockEnergyContainer {
    public InsertOnlyEnergyContainer(BlockEntity entity, int energyCapacity) {
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
