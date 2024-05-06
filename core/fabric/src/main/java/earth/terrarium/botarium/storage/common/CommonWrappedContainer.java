package earth.terrarium.botarium.storage.common;

import earth.terrarium.botarium.resources.TransferResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record CommonWrappedContainer<T, U extends TransferResource<T, U>, V extends TransferVariant<T>>(Storage<V> storage, Function<U, V> toVariant, Function<V, U> toResource) implements CommonStorage<U> {
    public U toResource(V variant) {
        return toResource.apply(variant);
    }

    public V toVariant(U resource) {
        return toVariant.apply(resource);
    }

    @Override
    public int getSlotCount() {
        if (storage instanceof SlottedStorage<V>) {
            return ((SlottedStorage<V>) storage).getSlotCount();
        }
        int count = 0;
        for (var ignored : storage) {
            count++;
        }
        return count;
    }

    @Override
    public @NotNull StorageSlot<U> getSlot(int slot) {
        if (storage instanceof SlottedStorage<V> slotted) {
            return new CommonWrappedSlotSlot<>(slotted.getSlot(slot), this::toVariant, this::toResource);
        }
        int i = 0;
        for (var view : storage) {
            if (i == slot) {
                return new CommonWrappedSlotSlot<>(view, this::toVariant, this::toResource);
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public long insert(U resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = storage.insert(toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(U resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = storage.extract(toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }
}
