package earth.terrarium.botarium.storage.fabric;

import earth.terrarium.botarium.resource.TransferResource;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public record FabricWrappedSlot<T, U extends TransferResource<T, U>, V extends TransferVariant<T>>(StorageSlot<U> container,
                                                                                                   @Nullable OptionalSnapshotParticipant<?> updateManager,
                                                                                                   Function<V, U> toUnit,
                                                                                                   Function<U, V> toVariant) implements SingleSlotStorage<V> {

    public FabricWrappedSlot(StorageSlot<U> container, Function<U, V> toVariant, Function<V, U> toUnit) {
        this(container, OptionalSnapshotParticipant.of(container), toUnit, toVariant);
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
