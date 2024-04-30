package earth.terrarium.botarium.storage.common;

import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record CommonWrappedContainer<T, U extends TransferUnit<T, U>, V extends TransferVariant<T>>(Storage<V> storage, Function<U, V> toVariant, Function<V, U> toUnit) implements CommonStorage<U> {
    public U toUnit(V variant) {
        return toUnit.apply(variant);
    }

    public V toVariant(U unit) {
        return toVariant.apply(unit);
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
            return new CommonWrappedSlotSlot<>(slotted.getSlot(slot), this::toVariant, this::toUnit);
        }
        int i = 0;
        for (var view : storage) {
            if (i == slot) {
                return new CommonWrappedSlotSlot<>(view, this::toVariant, this::toUnit);
            }
            i++;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public long insert(U unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = storage.insert(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(U unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = storage.extract(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }
}
