package earth.terrarium.botarium.common.item.wrappers;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record CommonItemContainer(IItemHandler handler) implements UnitContainer<ItemUnit> {
    @Override
    public int getSlotCount() {
        return handler.getSlots();
    }

    @Override
    public @NotNull UnitSlot<ItemUnit> getSlot(int slot) {
        return new DelegatingItemSlot(handler, slot);
    }

    public record DelegatingItemSlot(IItemHandler handler, int slot) implements UnitSlot<ItemUnit> {

        @Override
        public long getLimit() {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isValueValid(ItemUnit unit) {
            return handler.isItemValid(slot, unit.toStack());
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
        public long insert(ItemUnit unit, long amount, boolean simulate) {
            ItemStack leftover = handler.insertItem(slot, unit.toStack((int) amount), simulate);
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
