package earth.terrarium.botarium.neoforge.item;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record ForgeItemContainer<T extends ItemContainer & Updatable>(T container) implements IItemHandler {
    @Override
    public int getSlots() {
        return container.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return container.getStackInSlot(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean simulate) {
        ItemStack stack = container.insertIntoSlot(i, arg, simulate);
        if (!simulate) container.update();
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int j, boolean simulate) {
        ItemStack stack = container.extractFromSlot(i, j, simulate);
        if (!simulate) container.update();
        return stack;
    }

    @Override
    public int getSlotLimit(int i) {
        return container.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return container.isItemValid(i, arg);
    }
}
