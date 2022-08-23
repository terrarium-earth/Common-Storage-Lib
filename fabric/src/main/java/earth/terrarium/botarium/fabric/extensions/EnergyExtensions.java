package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.AbstractEnergy;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.msrandom.extensions.annotations.ClassExtension;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(AbstractEnergy.class)
public interface EnergyExtensions extends EnergyStorage {
    @Override
    default long insert(long maxAmount, TransactionContext transaction) {
        return ((AbstractEnergy) this).insertEnergy((int) maxAmount);
    }

    @Override
    default long extract(long maxAmount, TransactionContext transaction) {
        return ((AbstractEnergy) this).extractEnergy((int) maxAmount);
    }

    @Override
    default long getAmount() {
        return ((AbstractEnergy) this).getEnergyLevel();
    }

    @Override
    default long getCapacity() {
        return ((AbstractEnergy) this).getMaxCapacity();
    }

    @Override
    default boolean supportsExtraction() {
        return false;
    }
}
