package earth.terrarium.botarium.storage.context;

import earth.terrarium.botarium.resource.item.ItemResource;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ContextItemContainer(List<SingleSlotStorage<ItemVariant>> storage, TriFunction<ItemVariant, Long, TransactionContext, Long> insert) implements CommonStorage<ItemResource> {
    @Override
    public int getSlotCount() {
        return storage.size();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> getSlot(int slot) {
        return new StorageSlotImpl(storage.get(slot));
    }

    @Override
    public long insert(ItemResource unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = insert.apply(ConversionUtils.toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemResource predicate, long amount, boolean simulate) {
        long leftover = amount;
        ItemVariant variant = ConversionUtils.toVariant(predicate);
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

    public record StorageSlotImpl(SingleSlotStorage<ItemVariant> storage) implements StorageSlot<ItemResource> {

        @Override
        public long getLimit() {
            return storage.getCapacity();
        }

        @Override
        public boolean isValueValid(ItemResource value) {
            return true;
        }

        @Override
        public long insert(ItemResource unit, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = storage.insert(ConversionUtils.toVariant(unit), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        }

        @Override
        public long extract(ItemResource unit, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long extracted = storage.extract(ConversionUtils.toVariant(unit), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return extracted;
            }
        }

        @Override
        public ItemResource getUnit() {
            return ConversionUtils.toUnit(storage.getResource());
        }

        @Override
        public long getAmount() {
            return storage.getAmount();
        }

        @Override
        public boolean isBlank() {
            return storage.isResourceBlank();
        }
    }
}
