package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ContextItemContainer(List<SingleSlotStorage<ItemVariant>> storage) implements ItemContainer {
    public static ItemUnit of(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant of(ItemUnit unit) {
        return ItemVariant.of(unit.unit(), unit.components());
    }

    @Override
    public int getSlotCount() {
        return storage.size();
    }

    @Override
    public @NotNull ContainerSlot<ItemUnit> getSlot(int slot) {
        return new ContainerSlotImpl(storage.get(slot));
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        long leftover = amount;
        try (var transaction = Transaction.openOuter()) {
            for (SingleSlotStorage<ItemVariant> view : storage) {
                long inserted = view.insert(of(unit), leftover, transaction);
                leftover -= inserted;
            }
            if (!simulate) {
                transaction.commit();
            }
        }
        return amount - leftover;
    }

    @Override
    public long extract(ItemUnit predicate, long amount, boolean simulate) {
        long leftover = amount;
        ItemVariant variant = of(predicate);
        try (var transaction = Transaction.openOuter()) {
            for (SingleSlotStorage<ItemVariant> view : storage) {
                long extractedAmount = view.extract(variant, leftover, transaction);
                leftover -= extractedAmount;
                if (leftover <= 0) {
                    break;
                }
            }
            if (!simulate) {
                transaction.commit();
            }
        }
        return amount - leftover;
    }

    @Override
    public DataComponentPatch createSnapshot() {
        return DataComponentPatch.EMPTY;
    }

    @Override
    public void readSnapshot(DataComponentPatch snapshot) {
    }

    @Override
    public void update() {
    }

    public record ContainerSlotImpl(SingleSlotStorage<ItemVariant> storage) implements ContainerSlot<ItemUnit> {

        @Override
        public long getLimit() {
            return storage.getCapacity();
        }

        @Override
        public boolean isValueValid(ItemUnit value) {
            return true;
        }

        @Override
        public long insert(ItemUnit unit, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = storage.insert(of(unit), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        }

        @Override
        public long extract(ItemUnit unit, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long extracted = storage.extract(of(unit), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return extracted;
            }
        }

        @Override
        public DataComponentPatch createSnapshot() {
            return DataComponentPatch.EMPTY;
        }

        @Override
        public void readSnapshot(DataComponentPatch snapshot) {}

        @Override
        public void update() {}

        @Override
        public ItemUnit getUnit() {
            return of(storage.getResource());
        }

        @Override
        public long getAmount() {
            return storage.getAmount();
        }
    }
}
