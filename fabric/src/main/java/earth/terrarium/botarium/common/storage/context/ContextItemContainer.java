package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

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
    public @NotNull SingleSlotContainer<ItemUnit, ItemHolder> getSlot(int slot) {
        return new SingleSlotContainerImpl(storage.get(slot));
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
    public @NotNull ItemHolder extract(Predicate<ItemUnit> predicate, long amount, boolean simulate) {
        long leftover = amount;
        ItemVariant variant = null;
        try (var transaction = Transaction.openOuter()) {
            for (SingleSlotStorage<ItemVariant> view : storage) {
                if (variant == null && predicate.test(of(view.getResource()))) {
                    variant = view.getResource();
                }
                if (variant != null) {
                    long extractedAmount = view.extract(variant, leftover, transaction);
                    leftover -= extractedAmount;
                    if (leftover <= 0) {
                        break;
                    }
                }
            }
            if (!simulate) {
                transaction.commit();
            }
        }
        return variant == null ? ItemHolder.EMPTY : ItemHolder.of(of(variant), (int) (amount - leftover));
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

    public record SingleSlotContainerImpl(SingleSlotStorage<ItemVariant> storage) implements SingleSlotContainer<ItemUnit, ItemHolder> {

        @Override
        public long getLimit() {
            return storage.getCapacity();
        }

        @Override
        public boolean isValueValid(ItemUnit value) {
            return true;
        }

        @Override
        public long insert(ItemUnit value, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = storage.insert(ItemVariant.of(value.unit(), value.components()), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        }

        @Override
        public ItemHolder extract(Predicate<ItemUnit> predicate, long amount, boolean simulate) {
            ItemUnit unit = of(storage.getResource());
            if (predicate.test(unit)) {
                try (var transaction = Transaction.openOuter()) {
                    long extracted = storage.extract(ItemVariant.of(unit.unit(), unit.components()), amount, transaction);
                    if (!simulate) {
                        transaction.commit();
                    }
                    return ItemHolder.of(unit, (int) extracted);
                }
            }
            return ItemHolder.EMPTY;
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
        public long getHeldAmount() {
            return storage.getAmount();
        }
    }
}
