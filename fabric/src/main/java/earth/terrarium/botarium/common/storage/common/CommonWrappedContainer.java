package earth.terrarium.botarium.common.storage.common;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;

public abstract class CommonWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements UnitContainer<U> {
    private final Storage<V> storage;

    public CommonWrappedContainer(Storage<V> storage) {
        this.storage = storage;
    }

    public abstract U toUnit(V variant);

    public abstract V toVariant(U unit);

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
    public @NotNull ContainerSlot<U> getSlot(int slot) {
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
}
