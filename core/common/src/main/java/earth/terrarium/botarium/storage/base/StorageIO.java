package earth.terrarium.botarium.storage.base;

public interface StorageIO<T> {
    long insert(T unit, long amount, boolean simulate);

    long extract(T unit, long amount, boolean simulate);
}
