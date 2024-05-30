package earth.terrarium.botarium.storage.base;

import earth.terrarium.botarium.resources.Resource;
import org.jetbrains.annotations.NotNull;

public interface CommonStorage<T extends Resource> extends StorageIO<T> {
    int size();

    @NotNull
    StorageSlot<T> getSlot(int slot);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }

    default long getLimit(int index, T resource) {
        return getSlot(index).getLimit(resource);
    }

    default boolean isResourceValid(int index, T resource) {
        return getSlot(index).isResourceValid(resource);
    }

    default T getResource(int index) {
        return getSlot(index).getResource();
    }

    default long getAmount(int index) {
        return getSlot(index).getAmount();
    }

    default long insert(int index, T resource, long amount, boolean simulate) {
        return getSlot(index).insert(resource, amount, simulate);
    }

    default long extract(int index, T resource, long amount, boolean simulate) {
        return getSlot(index).extract(resource, amount, simulate);
    }

    @SuppressWarnings("unchecked")
    static <T extends Resource> Class<CommonStorage<T>> asClass() {
        return (Class<CommonStorage<T>>) (Object) CommonStorage.class;
    }
}
