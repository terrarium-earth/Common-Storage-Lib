package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.EnergyContainer;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.msrandom.extensions.annotations.ClassExtension;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(EnergyContainer.class)
public interface EnergyExtensions extends EnergyStorage {
    @Override
    default long insert(long maxAmount, TransactionContext transaction) {
        return ((EnergyContainer) this).insertEnergy((int) maxAmount);
    }

    @Override
    default long extract(long maxAmount, TransactionContext transaction) {
        return ((EnergyContainer) this).extractEnergy((int) maxAmount);
    }

    @Override
    default long getAmount() {
        return ((EnergyContainer) this).getStoredEnergy();
    }

    @Override
    default long getCapacity() {
        return ((EnergyContainer) this).getMaxCapacity();
    }

    @Override
    default boolean supportsExtraction() {
        return false;
    }
}
