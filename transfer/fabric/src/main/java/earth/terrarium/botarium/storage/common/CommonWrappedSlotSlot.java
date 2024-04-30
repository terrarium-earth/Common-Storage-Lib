package earth.terrarium.botarium.storage.common;

import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import java.util.function.Function;

public record CommonWrappedSlotSlot<T, U extends TransferUnit<T, U>, V extends TransferVariant<T>>(
        StorageView<V> view, Function<U, V> toVariant, Function<V, U> toUnit) implements StorageSlot<U> {

    @Override
    public long getLimit() {
        return view.getCapacity();
    }

    @Override
    public boolean isValueValid(U value) {
        return true;
    }

    @Override
    public long insert(U value, long amount, boolean simulate) {
        if (view instanceof SingleSlotStorage<V> slot) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = slot.insert(toVariant.apply(value), amount, transaction);
                if (!simulate) {
                    transaction.commit();
                }
                return inserted;
            }
        } else {
            return 0;
        }
    }

    @Override
    public long extract(U unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = view.extract(toVariant.apply(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public U getUnit() {
        return toUnit.apply(view.getResource());
    }

    @Override
    public long getAmount() {
        return view.getAmount();
    }

    @Override
    public boolean isBlank() {
        return view.isResourceBlank();
    }
}
