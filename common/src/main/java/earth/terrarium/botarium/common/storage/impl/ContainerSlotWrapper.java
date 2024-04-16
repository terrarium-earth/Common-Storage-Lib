package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.storage.base.SlottedContainer;

public record ContainerSlotWrapper<T>(SlottedContainer<T> container, int slot) implements SingleSlotContainer<T> {
    @Override
    public T getValue() {
        return container.getValueInSlot(slot);
    }

    @Override
    public int getLimit() {
        return container.getSlotLimit(slot);
    }

    @Override
    public boolean isValueValid(T value) {
        return container.isValueValid(slot, value);
    }

    @Override
    public long insert(T value, boolean simulate) {
        return container.insertIntoSlot(slot, value, simulate);
    }

    @Override
    public T extract(long amount, boolean simulate) {
        return container.extractFromSlot(slot, amount, simulate);
    }
}
