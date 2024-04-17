package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import org.jetbrains.annotations.NotNull;

public interface UnitContainer<T extends TransferUnit<?>> extends UpdateManager {
    int getSlotCount();

    @NotNull
    ContainerSlot<T> getSlot(int slot);

    long insert(T unit, long amount, boolean simulate);

    long extract(T unit, long amount, boolean simulate);

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }
}
