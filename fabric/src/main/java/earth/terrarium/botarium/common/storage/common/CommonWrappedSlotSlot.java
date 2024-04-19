package earth.terrarium.botarium.common.storage.common;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;

import java.util.function.Function;

public record CommonWrappedSlotSlot<T, U extends TransferUnit<T>, V extends TransferVariant<T>>(
        StorageView<V> view, Function<U, V> toVariant, Function<V, U> toUnit) implements UnitSlot<U> {

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
}
