package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface BasicContainer<T extends TransferUnit<?>, U extends UnitHolder<T>> extends UpdateManager {
    int getSlotCount();

    @NotNull
    SingleSlotContainer<T, U> getSlot(int slot);

    long insert(T unit, long amount, boolean simulate);

    @NotNull
    U extract(Predicate<T> predicate, long amount, boolean simulate);

    default long insertAndUpdate(T unit, long amount, boolean simulate) {
        long inserted = insert(unit, amount, simulate);
        if (!simulate) {
            update();
        }
        return inserted;
    }

    default @NotNull U extractAndUpdate(Predicate<T> predicate, long amount, boolean simulate) {
        U extracted = extract(predicate, amount, simulate);
        if (!simulate) {
            update();
        }
        return extracted;
    }

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }
}
