package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.component.DataComponentPatch;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class FabricWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements SlottedStorage<V> {
    private final UnitContainer<U> container;
    private final OptionalSnapshotParticipant<?> updateManager;
    private final Function<U, V> toVariant;
    private final Function<V, U> toUnit;

    public FabricWrappedContainer(UnitContainer<U> container, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.container = container;
        this.toVariant = toVariant;
        this.toUnit = toUnit;

        if (container instanceof UpdateManager<?> updater) {
            this.updateManager = new OptionalSnapshotParticipant<>(updater);
        } else {
            this.updateManager = null;
        }
    }

    public U toUnit(V variant) {
        return toUnit.apply(variant);
    }

    public V toVariant(U unit) {
        return toVariant.apply(unit);
    }

    @Override
    public long insert(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toUnit(resource);
        updateSnapshots(transaction);
        return container.insert(holder, maxAmount, false);
    }

    @Override
    public long extract(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toUnit(resource);
        updateSnapshots(transaction);
        return container.extract(holder, maxAmount, false);
    }

    @Override
    public @NotNull Iterator<StorageView<V>> iterator() {
        return new Iterator<>() {
            int slot = 0;

            @Override
            public boolean hasNext() {
                return slot < container.getSlotCount();
            }

            @Override
            public StorageView<V> next() {
                return FabricWrappedContainer.this.getSlot(slot++);
            }
        };
    }

    @Override
    public int getSlotCount() {
        return container.getSlotCount();
    }

    @Override
    public SingleSlotStorage<V> getSlot(int slot) {
        UnitSlot<U> unitSlot = container.getSlot(slot);
        return new FabricWrappedSlot<>(unitSlot, this::toVariant, this::toUnit);
    }

    public UnitContainer<U> getContainer() {
        return container;
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (updateManager != null) {
            updateManager.updateSnapshots(transaction);
        }
    }
}
