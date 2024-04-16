package earth.terrarium.botarium.common.item.container;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class FabrictemContainer<T> extends SnapshotParticipant<T> implements SlottedStorage<ItemVariant> {
    public final ItemContainer<T> container;

    public FabrictemContainer(ItemContainer<T> container) {
        this.container = container;
    }

    @Override
    public int getSlotCount() {
        return container.getSlotCount();
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        return new FabricSingleSlotWrapper<>(container, slot);
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insert(resource.toStack((int) maxAmount), false);
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long extracted = 0;
        for (int i = 0; i < container.getSlotCount(); i++) {
            if (resource.matches(container.getValueInSlot(i))) {
                extracted += container.extractFromSlot(i, (int) maxAmount, false).getCount();
                if (extracted >= maxAmount) {
                    break;
                }
            }
        }
        return extracted;
    }

    @Override
    public @NotNull Iterator<StorageView<ItemVariant>> iterator() {
        return new Iterator<>() {
            private int slot = 0;

            @Override
            public boolean hasNext() {
                return slot < container.getSlotCount();
            }

            @Override
            public StorageView<ItemVariant> next() {
                return new FabricSingleSlotWrapper<>(container, slot++);
            }
        };
    }

    @Override
    protected T createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(T snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    public boolean supportsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    protected void onFinalCommit() {
        container.update();
    }
}
