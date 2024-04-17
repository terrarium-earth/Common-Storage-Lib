package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.UpdateManager;

public interface ValueContainer extends UpdateManager {

    long getStoredAmount();

    long getCapacity();

    boolean allowsInsertion();

    boolean allowsExtraction();

    long insert(long amount, boolean simulate);

    long extract(long amount, boolean simulate);
}
