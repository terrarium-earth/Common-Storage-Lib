package earth.terrarium.common_storage_lib.storage.context;

import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.ConversionUtils;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ContextItemContainer(List<SingleSlotStorage<ItemVariant>> storage, TriFunction<ItemVariant, Long, TransactionContext, Long> insert) implements CommonStorage<ItemResource> {
    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> get(int index) {
        return new StorageSlotImpl(storage.get(index));
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = insert.apply(ConversionUtils.toVariant(resource), amount, transaction);
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
        public long getLimit(ItemResource resource) {
            return storage.getCapacity();
        }

        @Override
        public boolean isResourceValid(ItemResource value) {
            return true;
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = storage.insert(ConversionUtils.toVariant(resource), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long extracted = storage.extract(ConversionUtils.toVariant(resource), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return extracted;
            }
        }

        @Override
        public ItemResource getResource() {
            return ConversionUtils.toResource(storage.getResource());
        }

        @Override
        public long getAmount() {
            return storage.getAmount();
        }
    }
}
