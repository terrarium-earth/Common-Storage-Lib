package earth.terrarium.botarium.storage.base;

import org.jetbrains.annotations.NotNull;

public interface CommonStorage<T> extends StorageIO<T> {
    int getSlotCount();

    @NotNull
    StorageSlot<T> getSlot(int slot);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }

    @SuppressWarnings("unchecked")
    static <T> Class<CommonStorage<T>> asClass() {
        return (Class<CommonStorage<T>>) (Object) CommonStorage.class;
    }
}
