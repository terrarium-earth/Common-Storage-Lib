package earth.terrarium.common_storage_lib.storage.base;

import earth.terrarium.common_storage_lib.resources.Resource;
import earth.terrarium.common_storage_lib.resources.ResourceStack;

public interface StorageSlot<T extends Resource> extends StorageIO<T> {
    long getLimit(T resource);

    T getResource();

    default ResourceStack<T> getContents() {
        return new ResourceStack<>(getResource(), getAmount());
    }

    boolean isResourceValid(T resource);

    long getAmount();
}
