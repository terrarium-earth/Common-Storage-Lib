package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
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

public final class FabricWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>, S, C extends UnitContainer<U>> extends SnapshotParticipant<S> implements SlottedStorage<V> {
    private final C container;
    private final UpdateManager<S> updateManager;
    private final Function<U, V> toVariant;
    private final Function<V, U> toUnit;

    public FabricWrappedContainer(C container, UpdateManager<S> updateManager, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.container = container;
        this.updateManager = updateManager;
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
    protected S createSnapshot() {
        return updateManager.createSnapshot();
    }

    @Override
    protected void readSnapshot(S snapshot) {
        updateManager.readSnapshot(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        updateManager.update();
    }

    @Override
    public int getSlotCount() {
        return container.getSlotCount();
    }

    @Override
    public SingleSlotStorage<V> getSlot(int slot) {
        UnitSlot<U> unitSlot = container.getSlot(slot);
        if (unitSlot instanceof UpdateManager<?> updater) {
            return new FabricWrappedSlot<>(unitSlot, updater, this::toUnit, this::toVariant);
        }
        throw new IllegalArgumentException("UnitSlot must implement UpdateManager");
    }

    public C getContainer() {
        return container;
    }
}
