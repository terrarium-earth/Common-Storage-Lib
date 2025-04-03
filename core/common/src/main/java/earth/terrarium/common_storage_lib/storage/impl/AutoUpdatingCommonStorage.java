package earth.terrarium.common_storage_lib.storage.impl;

import earth.terrarium.common_storage_lib.resources.Resource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import org.jetbrains.annotations.NotNull;

public record AutoUpdatingCommonStorage<T extends Resource>(CommonStorage<T> commonStorage) implements CommonStorage<T> {
    @Override
    public int size() {
        return commonStorage.size();
    }

    @Override
    public @NotNull StorageSlot<T> get(int index) {
        return commonStorage.get(index);
    }

    @Override
    public long insert(T resource, long amount, boolean simulate) {
        var result = commonStorage.insert(resource, amount, simulate);
        if (!simulate) {
            UpdateManager.batch(commonStorage);
        }
        return result;
    }

    @Override
    public long extract(T resource, long amount, boolean simulate) {
        var result = commonStorage.extract(resource, amount, simulate);
        if (!simulate) {
            UpdateManager.batch(commonStorage);
        }
        return result;
    }
}
