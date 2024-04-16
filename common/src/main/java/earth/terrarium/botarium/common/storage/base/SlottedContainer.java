package earth.terrarium.botarium.common.storage.base;

import org.jetbrains.annotations.NotNull;

public interface SlottedContainer<T> extends BasicContainer<T> {

    int getSlotCount();

    @NotNull
    T getValueInSlot(int slot);

    int getSlotLimit(int slot);

    boolean isValueValid(int slot, @NotNull T value);

    long insertIntoSlot(int slot, @NotNull T value, boolean simulate);

    @NotNull
    T extractFromSlot(int slot, long amount, boolean simulate);
}
