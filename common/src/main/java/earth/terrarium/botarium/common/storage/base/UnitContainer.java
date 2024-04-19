package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import org.jetbrains.annotations.NotNull;

public interface UnitContainer<T extends TransferUnit<?>> extends UnitIO<T> {
    int getSlotCount();

    @NotNull
    UnitSlot<T> getSlot(int slot);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }

    @Override
    default long insert(T unit, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, unit, amount, simulate);
    }

    @Override
    default long extract(T unit, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, unit, amount, simulate);
    }

    @SuppressWarnings("unchecked")
    static <T extends TransferUnit<?>> Class<UnitContainer<T>> asClass() {
        return (Class<UnitContainer<T>>) (Object) UnitContainer.class;
    }
}
