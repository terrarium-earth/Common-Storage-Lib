package earth.terrarium.botarium.storage.base;

import earth.terrarium.botarium.resources.Resource;

public interface StorageSlot<T extends Resource> extends StorageIO<T> {
    long getLimit(T resource);

    boolean isResourceValid(T resource);

    T getResource();

    long getAmount();
}
