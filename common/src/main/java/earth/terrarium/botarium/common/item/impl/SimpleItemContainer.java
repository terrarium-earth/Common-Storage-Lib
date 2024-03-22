package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import earth.terrarium.botarium.util.Serializable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleItemContainer implements ItemContainer, Serializable {
    NonNullList<ItemStack> stacks;

    public SimpleItemContainer(int capacity) {
        this.stacks = NonNullList.withSize(capacity, ItemStack.EMPTY);
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (!isItemValid(0, stack)) return stack;
        ItemStack leftover = stack.copy();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack itemStack = stacks.get(i);
            if (itemStack.isEmpty()) {
                int amount = Math.min(leftover.getCount(), getSlotLimit(0));
                if (!simulate) {
                    itemStack = leftover.copy();
                    itemStack.setCount(amount);
                    stacks.set(i, itemStack);
                }
                leftover.shrink(amount);
                if (leftover.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            } else if (ItemStack.isSameItemSameTags(leftover, itemStack)) {
                int amount = Math.min(leftover.getCount(), getSlotLimit(0) - itemStack.getCount());
                if (!simulate) {
                    itemStack.grow(amount);
                    stacks.set(i, itemStack);
                }
                leftover.shrink(amount);
                if (leftover.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return leftover;
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        if (!isItemValid(slot, stack)) return stack;
        ItemStack itemStack = stacks.get(slot);
        if (itemStack.isEmpty()) {
            int amount = Math.min(stack.getCount(), getSlotLimit(slot));
            if (!simulate) {
                itemStack = stack.copy();
                itemStack.setCount(amount);
                stacks.set(slot, itemStack);
            }
            stack.shrink(amount);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        } else if (ItemStack.isSameItemSameTags(stack, itemStack)) {
            int amount = Math.min(stack.getCount(), getSlotLimit(slot) - itemStack.getCount());
            if (!simulate) {
                itemStack.grow(amount);
                stacks.set(slot, itemStack);
            }
            stack.shrink(amount);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        ItemStack extracted = ItemStack.EMPTY;
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (!stack.isEmpty()) {
                int toExtract = Math.min(amount - extracted.getCount(), stack.getCount());
                if (extracted.isEmpty()) {
                    extracted = stack.copyWithCount(toExtract);
                } else if (ItemStack.isSameItemSameTags(extracted, stack)) {
                    extracted.grow(toExtract);
                }
                if (!simulate) {
                    stack.shrink(toExtract);
                    stacks.set(i, stack);
                }
                if (extracted.getCount() >= amount) {
                    break;
                }
            }
        }
        return extracted;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        ItemStack stack = stacks.get(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        int toExtract = Math.min(amount, stack.getCount());
        ItemStack extracted = stack.copyWithCount(toExtract);
        if (!simulate) {
            stack.shrink(toExtract);
            stacks.set(slot, stack);
        }
        return extracted;
    }

    @Override
    public boolean isEmpty() {
        return stacks.isEmpty();
    }

    @Override
    public ItemSnapshot createSnapshot() {
        return new SimpleItemSnapshot(stacks);
    }

    @Override
    public void loadSnapshot(ItemSnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }

    @Override
    public void clearContent() {
        stacks.clear();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, stacks);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return ContainerHelper.saveAllItems(nbt, stacks);
    }
}
