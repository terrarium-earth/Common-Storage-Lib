package earth.terrarium.botarium.forge.item;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record PlatformItemContainer(IItemHandler itemHandler) implements ItemContainer {

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return itemHandler.isItemValid(slot, stack);
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        ItemStack leftover = stack;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            stack = itemHandler.insertItem(i, leftover, simulate);
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (stack.getCount() < leftover.getCount()) {
                leftover = stack;
            }
        }
        return leftover;
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        return itemHandler.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        ItemStack extracted = ItemStack.EMPTY;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            extracted = itemHandler.extractItem(i, amount, simulate);
            if (!extracted.isEmpty()) {
                return extracted;
            }
        }
        return extracted;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemSnapshot createSnapshot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadSnapshot(ItemSnapshot snapshot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);
        }
    }
}
