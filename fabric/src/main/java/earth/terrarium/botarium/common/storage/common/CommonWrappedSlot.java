package earth.terrarium.botarium.common.storage.common;

import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;

import java.util.function.Predicate;

public record CommonWrappedSlot<T, U extends TransferUnit<T>, V extends TransferVariant<T>, H extends UnitHolder<U>>(
        CommonWrappedContainer<T, U, V, H> container, StorageView<V> view) implements SingleSlotContainer<U, H> {

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
                long inserted = slot.insert(view.getResource(), amount, transaction);
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
    public H extract(Predicate<U> predicate, long amount, boolean simulate) {
        if (view.isResourceBlank() || !predicate.test(container.fromVariant(view.getResource()))) return container.createHolder(null, 0);
        try (var transaction = Transaction.openOuter()) {
            long extracted = view.extract(view.getResource(), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return container.createHolder(null, extracted);
        }
    }

    @Override
    public DataComponentPatch createSnapshot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readSnapshot(DataComponentPatch snapshot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }

    @Override
    public U getUnit() {
        return container.fromVariant(view.getResource());
    }

    @Override
    public long getHeldAmount() {
        return view.getAmount();
    }
}
