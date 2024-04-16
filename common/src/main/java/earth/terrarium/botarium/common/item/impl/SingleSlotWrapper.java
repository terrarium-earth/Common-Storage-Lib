package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import net.minecraft.world.item.ItemStack;

public class SingleSlotWrapper<T> implements SingleSlotContainer<ItemStack> {
    ItemContainer<T> container;
    int slot;

    public SingleSlotWrapper(ItemContainer<T> container, int slot) {
        this.container = container;
        this.slot = slot;
    }

    @Override
    public long insert(ItemStack value, boolean simulate) {
        long inserted = container.insertIntoSlot(slot, value, simulate);
        if (!simulate) container.update();
        return inserted;
    }

    @Override
    public ItemStack extract(long amount, boolean simulate) {
        ItemStack itemStack = container.extractFromSlot(slot, amount, simulate);
        if (!simulate) container.update();
        return itemStack;
    }

    @Override
    public ItemStack getValue() {
        return container.getValueInSlot(slot);
    }

    @Override
    public int getLimit() {
        return container.getSlotLimit(slot);
    }

    @Override
    public boolean isValueValid(ItemStack value) {
        return container.isValueValid(slot, value);
    }
}
