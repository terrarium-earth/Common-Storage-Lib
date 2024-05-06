package earth.terrarium.botarium.storage.base;

public interface StorageSlot<T> extends StorageIO<T> {
    long getLimit();

    boolean isValueValid(T resource);

    T getResource();

    long getAmount();

    boolean isBlank();
}
