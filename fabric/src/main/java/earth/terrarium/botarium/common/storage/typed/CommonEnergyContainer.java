package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import team.reborn.energy.api.EnergyStorage;

public record CommonEnergyContainer(EnergyStorage storage) implements LongContainer {
    @Override
    public long getStoredAmount() {
        return storage.getAmount();
    }

    @Override
    public long getCapacity() {
        return storage.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return storage.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return storage.supportsExtraction();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = storage.insert(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = storage.extract(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public DataComponentPatch createSnapshot() {
        return DataComponentPatch.EMPTY;
    }

    @Override
    public void readSnapshot(DataComponentPatch snapshot) {}

    @Override
    public void update() {}
}
