package earth.terrarium.botarium.common.storage.common;

import earth.terrarium.botarium.common.storage.base.BasicContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class CommonWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>, H extends UnitHolder<U>> implements BasicContainer<U, H> {
    private final Storage<V> storage;

    public CommonWrappedContainer(Storage<V> storage) {
        this.storage = storage;
    }

    public abstract U fromVariant(V variant);

    public abstract V toVariant(U unit);

    public abstract H createHolder(@Nullable U unit, long amount);

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
    public @NotNull SingleSlotContainer<U, H> getSlot(int slot) {
        if (storage instanceof SlottedStorage<V> slotted) {
            return new CommonWrappedSlot<>(this, slotted.getSlot(slot));
        }
        int i = 0;
        for (var view : storage) {
            if (i == slot) {
                return new CommonWrappedSlot<>(this, view);
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
    public @NotNull H extract(Predicate<U> predicate, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            U unit = null;
            for (var view : storage.nonEmptyViews()) {
                if (!predicate.test(fromVariant(view.getResource()))) continue;
                long extracted = view.extract(view.getResource(), amount, transaction);
                if (extracted > 0) {
                    unit = fromVariant(view.getResource());
                    break;
                }
            }
            return createHolder(unit, amount);
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
