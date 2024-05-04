package earth.terrarium.botarium.storage.base;

public interface StorageSlot<T> extends StorageIO<T> {
    long getLimit();

    boolean isValueValid(T unit);

    T getUnit();

    long getAmount();

    boolean isBlank();
}
