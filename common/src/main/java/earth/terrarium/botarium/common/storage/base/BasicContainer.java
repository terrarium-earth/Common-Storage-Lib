package earth.terrarium.botarium.common.storage.base;

import net.msrandom.multiplatform.annotations.Expect;

public interface BasicContainer<T> {
    long insert(T value, boolean simulate);

    T extract(long amount, boolean simulate);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }
}
