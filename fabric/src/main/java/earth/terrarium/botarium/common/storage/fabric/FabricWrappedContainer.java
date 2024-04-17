package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
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

public abstract class FabricWrappedContainer<T, U extends TransferUnit<T>, V extends TransferVariant<T>, C extends UnitContainer<U>> extends SnapshotParticipant<DataComponentPatch> implements SlottedStorage<V> {
    private final C container;

    public FabricWrappedContainer(C container) {
        this.container = container;
    }

    public abstract U toUnit(V variant);

    public abstract V toVariant(U unit);

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
    protected DataComponentPatch createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(DataComponentPatch snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        container.update();
    }

    @Override
    public int getSlotCount() {
        return container.getSlotCount();
    }

    @Override
    public SingleSlotStorage<V> getSlot(int slot) {
        return new FabricWrappedSlot<>(container.getSlot(slot), this::toUnit, this::toVariant);
    }

    public C getContainer() {
        return container;
    }
}
