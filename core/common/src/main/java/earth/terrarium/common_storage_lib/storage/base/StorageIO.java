package earth.terrarium.common_storage_lib.storage.base;

public interface StorageIO<T> {
    long insert(T resource, long amount, boolean simulate);

    long extract(T resource, long amount, boolean simulate);
}
