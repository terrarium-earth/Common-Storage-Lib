package earth.terrarium.botarium.common.generic.utils;

import org.jetbrains.annotations.NotNull;

public interface StackBasedContainer<T> {

    int getSlotCount();

    @NotNull
    T getValueInSlot(int slot);

    int getSlotLimit(int slot);

    boolean isValueValid(int slot, @NotNull T value);

    long insert(@NotNull T value, boolean simulate);

    long insertIntoSlot(int slot, @NotNull T value, boolean simulate);

    @NotNull
    T extract(long amount, boolean simulate);

    @NotNull
    T extractFromSlot(int slot, long amount, boolean simulate);
}
