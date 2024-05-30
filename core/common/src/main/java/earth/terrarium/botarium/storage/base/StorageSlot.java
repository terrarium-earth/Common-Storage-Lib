package earth.terrarium.botarium.storage.base;

import earth.terrarium.botarium.resources.Resource;
import earth.terrarium.botarium.resources.ResourceStack;

public interface StorageSlot<T extends Resource> extends StorageIO<T> {
    long getLimit(T resource);

    T getResource();

    default ResourceStack<T> getContents() {
        return new ResourceStack<>(getResource(), getAmount());
    }

    boolean isResourceValid(T resource);

    long getAmount();
}
