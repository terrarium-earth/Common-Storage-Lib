package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.EnergyHoldable;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.msrandom.extensions.annotations.ClassExtension;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(EnergyHoldable.class)
public interface EnergyExtensions extends EnergyStorage {
    @Override
    default long insert(long maxAmount, TransactionContext transaction) {
        return ((EnergyHoldable) this).insertEnergy((int) maxAmount);
    }

    @Override
    default long extract(long maxAmount, TransactionContext transaction) {
        return ((EnergyHoldable) this).extractEnergy((int) maxAmount);
    }

    @Override
    default long getAmount() {
        return ((EnergyHoldable) this).getStoredEnergy();
    }

    @Override
    default long getCapacity() {
        return ((EnergyHoldable) this).getMaxCapacity();
    }

    @Override
    default boolean supportsExtraction() {
        return false;
    }
}
