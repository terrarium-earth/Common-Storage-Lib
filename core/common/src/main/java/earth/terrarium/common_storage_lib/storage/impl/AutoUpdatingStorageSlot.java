package earth.terrarium.common_storage_lib.storage.impl;

import earth.terrarium.common_storage_lib.resources.Resource;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;

public record AutoUpdatingStorageSlot<T extends Resource>(StorageSlot<T> slot) implements StorageSlot<T> {
    @Override
    public long getLimit(T resource) {
        return slot.getLimit(resource);
    }

    @Override
    public T getResource() {
        return slot.getResource();
    }

    @Override
    public boolean isResourceValid(T resource) {
        return slot.isResourceValid(resource);
    }

    @Override
    public long getAmount() {
        return slot.getAmount();
    }

    @Override
    public long insert(T resource, long amount, boolean simulate) {
        var result = slot.insert(resource, amount, simulate);
        if (!simulate) {
            UpdateManager.batch(slot);
        }
        return result;
    }

    @Override
    public long extract(T resource, long amount, boolean simulate) {
        var result = slot.extract(resource, amount, simulate);
        if (!simulate) {
            UpdateManager.batch(slot);
        }
        return result;
    }
}
