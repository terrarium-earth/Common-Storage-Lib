package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record VanillaContainerWrapper(Container container) implements SlottedContainer<ItemStack> {

    @Override
    public int getSlotCount() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull ItemStack getValueInSlot(int slot) {
        return container.getItem(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getMaxStackSize(getValueInSlot(slot));
    }

    @Override
    public boolean isValueValid(int slot, @NotNull ItemStack value) {
        return container.canPlaceItem(slot, value);
    }

    @Override
    public long insertIntoSlot(int slot, @NotNull ItemStack value, boolean simulate) {
        ItemStack valueInSlot = getValueInSlot(slot);
        if (valueInSlot.isEmpty()) {
            if (!simulate) {
                container.setItem(slot, value);
            }
            return value.getCount();
        } else if (ItemStack.isSameItemSameComponents(valueInSlot, value)) {
            long inserted = Math.min(valueInSlot.getMaxStackSize() - valueInSlot.getCount(), value.getCount());
            if (!simulate) {
                valueInSlot.grow((int) inserted);
                container.setItem(slot, valueInSlot);
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, long amount, boolean simulate) {
        ItemStack valueInSlot = getValueInSlot(slot);
        if (valueInSlot.isEmpty()) {
            return ItemStack.EMPTY;
        }
        long extracted = Math.min(valueInSlot.getCount(), amount);
        if (!simulate) {
            ItemStack extractedStack = valueInSlot.copy();
            extractedStack.setCount((int) extracted);
            valueInSlot.shrink((int) extracted);
            container.setItem(slot, valueInSlot);
            return extractedStack;
        }
        return valueInSlot.copy();
    }

    @Override
    public long insert(ItemStack value, boolean simulate) {
        ItemStack toInsert = value.copy();
        for (int i = 0; i < getSlotCount(); i++) {
            if (isValueValid(i, toInsert)) {
                long inserted = insertIntoSlot(i, toInsert, simulate);
                toInsert.shrink((int) inserted);
                if (toInsert.isEmpty()) {
                    return value.getCount();
                }
            }
        }
        return value.getCount() - toInsert.getCount();
    }

    @Override
    public ItemStack extract(long amount, boolean simulate) {
        ItemStack extracted = ItemStack.EMPTY;
        for (int i = 0; i < getSlotCount(); i++) {
            extracted = extractFromSlot(i, amount, simulate);
            if (!extracted.isEmpty()) {
                return extracted;
            }
        }
        return extracted;
    }
}
