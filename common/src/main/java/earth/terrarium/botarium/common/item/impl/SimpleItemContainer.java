package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Snapshotable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class SimpleItemContainer implements ItemContainer, Serializable, Snapshotable<ItemSnapshot> {
    private final NonNullList<ItemStack> stacks;
    private Runnable onUpdate = () -> {};

    public SimpleItemContainer(int capacity) {
        this.stacks = NonNullList.withSize(capacity, ItemStack.EMPTY);
    }

    public SimpleItemContainer(int capacity, ItemStack stack) {
        this(capacity);
        onUpdate = () -> serialize(stack.getOrCreateTag());
        this.deserialize(stack.getOrCreateTag());
    }

    public SimpleItemContainer(int capacity, Level level, BlockPos blockPos) {
        this(capacity);
        onUpdate = () -> {
            if (level != null) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity != null) {
                    blockEntity.setChanged();
                }
            }
        };
    }

    public SimpleItemContainer(int capacity, BlockEntity blockEntity) {
        this(capacity);
        onUpdate = blockEntity::setChanged;
    }

    public SimpleItemContainer(int capacity, Runnable onUpdate) {
        this(capacity);
        this.onUpdate = onUpdate;
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
        return getStackInSlot(slot).getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !isItemValid(0, stack)) return ItemStack.EMPTY;
        int insertedAmount = 0;
        ItemStack initial = stack.copy();
        for (int i = 0; i < stacks.size(); i++) {
            insertedAmount += insertIntoSlot(i, stack, simulate).getCount();
            stack = stack.copyWithCount(stack.getCount() - insertedAmount);
            if (insertedAmount >= initial.getCount()) {
                break;
            }
        }
        return initial.copyWithCount(insertedAmount);
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !isItemValid(slot, stack)) return ItemStack.EMPTY;
        ItemStack itemStack = stacks.get(slot);
        if (itemStack.isEmpty()) {
            int amount = Math.min(stack.getCount(), getSlotLimit(slot));
            itemStack = stack.copyWithCount(amount);
            if (!simulate) {
                stacks.set(slot, itemStack);
            }
            return itemStack;
        } else if (ItemStack.isSameItemSameTags(stack, itemStack)) {
            int amount = Math.min(stack.getCount(), itemStack.getMaxStackSize() - itemStack.getCount());
            if (!simulate) {
                itemStack.grow(amount);
                stacks.set(slot, itemStack);
            }
            return stack.copyWithCount(amount);
        }
        return ItemStack.EMPTY;
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
            stacks.set(slot, stacks.get(slot).copyWithCount(stack.getCount() - toExtract));
        }
        return extracted;
    }

    @Override
    public boolean isEmpty() {
        return stacks.isEmpty();
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

    @Override
    public SimpleItemSnapshot createSnapshot() {
        return new SimpleItemSnapshot();
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    public class SimpleItemSnapshot implements ItemSnapshot {
        CompoundTag tag;

        public SimpleItemSnapshot() {
            this.tag = SimpleItemContainer.this.serialize(new CompoundTag());
        }

        @Override
        public void loadSnapshot() {
            SimpleItemContainer.this.deserialize(tag);
        }
    }
}
