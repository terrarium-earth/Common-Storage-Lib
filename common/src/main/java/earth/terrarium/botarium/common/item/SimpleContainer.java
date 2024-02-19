package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.util.BlockProvider;
import earth.terrarium.botarium.util.Serializable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SimpleContainer implements ItemContainer, Serializable, BlockProvider<ItemSnapshot> {
    private final NonNullList<ItemStack> stacks;

    public SimpleContainer(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
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
        return stacks.get(slot).getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        return null;
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return null;
    }

    @Override
    public void clearContent() {
        stacks.clear();
    }

    @Override
    public ItemSnapshot createSnapshot() {
        return null;
    }

    @Override
    public void loadSnapshot(ItemSnapshot snapshot) {

    }

    @Override
    public void update() {

    }
}
