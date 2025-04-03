package earth.terrarium.common_storage_lib.energy.impl;

import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;

public record AutoUpdatingValueStorage(ValueStorage valueStorage) implements ValueStorage {
    @Override
    public long getStoredAmount() {
        return valueStorage.getStoredAmount();
    }

    @Override
    public long getCapacity() {
        return valueStorage.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return valueStorage.allowsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return valueStorage.allowsExtraction();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long insert = valueStorage.insert(amount, simulate);
        if (!simulate) {
            UpdateManager.batch(valueStorage);
        }
        return insert;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = valueStorage.extract(amount, simulate);
        if (!simulate) {
            UpdateManager.batch(valueStorage);
        }
        return extracted;
    }
}
