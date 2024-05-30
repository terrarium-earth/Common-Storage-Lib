package earth.terrarium.botarium.storage.base;

public interface ValueStorage {
    long getStoredAmount();

    long getCapacity();

    boolean allowsInsertion();

    boolean allowsExtraction();

    long insert(long amount, boolean simulate);

    long extract(long amount, boolean simulate);
}