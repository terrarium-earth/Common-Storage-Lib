package earth.terrarium.botarium.common.storage.common;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class CommonWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements UnitContainer<U> {
    private final Storage<V> storage;
    private final Function<U, V> toVariant;
    private final Function<V, U> toUnit;

    public CommonWrappedContainer(Storage<V> storage, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.storage = storage;
        this.toVariant = toVariant;
        this.toUnit = toUnit;
    }

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
    public @NotNull UnitSlot<U> getSlot(int slot) {
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
