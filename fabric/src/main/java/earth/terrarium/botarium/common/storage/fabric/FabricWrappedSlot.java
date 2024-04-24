package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.component.DataComponentPatch;

import java.util.function.Function;

public class FabricWrappedSlot<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements SingleSlotStorage<V> {
    private final UnitSlot<U> container;
    private final OptionalSnapshotParticipant<?> updateManager;
    private final Function<V, U> toUnit;
    private final Function<U, V> toVariant;

    public FabricWrappedSlot(UnitSlot<U> container, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.container = container;
        this.toVariant = toVariant;
        this.toUnit = toUnit;

        if (container instanceof UpdateManager<?> updater) {
            this.updateManager = new OptionalSnapshotParticipant<>(updater);
        } else {
            this.updateManager = null;
        }
    }

    @Override
    public long insert(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toUnit.apply(resource);
        updateSnapshots(transaction);
        return container.insert(holder, maxAmount, false);
    }

    @Override
    public long extract(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toUnit.apply(resource);
        updateSnapshots(transaction);
        return container.extract(holder, maxAmount, false);
    }

    @Override
    public boolean isResourceBlank() {
        return container.getUnit().isBlank();
    }

    @Override
    public V getResource() {
        return toVariant.apply(container.getUnit());
    }

    @Override
    public long getAmount() {
        return container.getAmount();
    }

    @Override
    public long getCapacity() {
        return container.getLimit();
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (updateManager != null) {
            updateManager.updateSnapshots(transaction);
        }
    }
}
