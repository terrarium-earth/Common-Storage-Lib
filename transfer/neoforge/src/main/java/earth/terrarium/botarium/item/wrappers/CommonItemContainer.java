package earth.terrarium.botarium.item.wrappers;

import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record CommonItemContainer(IItemHandler handler) implements CommonStorage<ItemUnit> {
    @Override
    public int getSlotCount() {
        return handler.getSlots();
    }

    @Override
    public @NotNull StorageSlot<ItemUnit> getSlot(int slot) {
        return new DelegatingItemSlot(handler, slot);
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, unit, amount, simulate);
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, unit, amount, simulate);
    }

    public record DelegatingItemSlot(IItemHandler handler, int slot) implements StorageSlot<ItemUnit> {

        @Override
        public long getLimit() {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isValueValid(ItemUnit unit) {
            return handler.isItemValid(slot, unit.toItemStack());
        }

        @Override
        public ItemUnit getUnit() {
            return ItemUnit.of(handler.getStackInSlot(slot));
        }

        @Override
        public long getAmount() {
            return handler.getStackInSlot(slot).getCount();
        }

        @Override
        public boolean isBlank() {
            return handler.getStackInSlot(slot).isEmpty();
        }

        @Override
        public long insert(ItemUnit unit, long amount, boolean simulate) {
            ItemStack leftover = handler.insertItem(slot, unit.toItemStack((int) amount), simulate);
            return amount - leftover.getCount();
        }

        @Override
        public long extract(ItemUnit unit, long amount, boolean simulate) {
            if (!unit.matches(handler.getStackInSlot(slot))) {
                return 0;
            }
            ItemStack extracted = handler.extractItem(slot, (int) amount, simulate);
            return extracted.getCount();
        }
    }
}
