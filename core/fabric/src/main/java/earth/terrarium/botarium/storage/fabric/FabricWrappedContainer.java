package earth.terrarium.botarium.storage.fabric;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.resources.TransferResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class FabricWrappedContainer<T, U extends TransferResource<T, U>, V extends TransferVariant<T>> implements SlottedStorage<V> {
    private final CommonStorage<U> container;
    private final OptionalSnapshotParticipant<?> updateManager;
    private final Function<U, V> toVariant;
    private final Function<V, U> toResource;

    public FabricWrappedContainer(
            CommonStorage<U> container, OptionalSnapshotParticipant<?> updateManager, Function<U, V> toVariant,
            Function<V, U> toResource) {
        this.container = container;
        this.updateManager = updateManager;
        this.toVariant = toVariant;
        this.toResource = toResource;
    }

    public FabricWrappedContainer(CommonStorage<U> container, Function<U, V> toVariant, Function<V, U> toResource) {
        this(container, OptionalSnapshotParticipant.of(container), toVariant, toResource);
    }

    public U toResource(V variant) {
        return toResource.apply(variant);
    }

    public V toVariant(U resource) {
        return toVariant.apply(resource);
    }

    @Override
    public long insert(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toResource(resource);
        updateSnapshots(transaction);
        return container.insert(holder, maxAmount, false);
    }

    @Override
    public long extract(V resource, long maxAmount, TransactionContext transaction) {
        U holder = toResource(resource);
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
        StorageSlot<U> storageSlot = container.getSlot(slot);
        return new FabricWrappedSlot<>(storageSlot, this::toVariant, this::toResource);
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (updateManager != null) {
            updateManager.updateSnapshots(transaction);
        }
    }

    public CommonStorage<U> container() {
        return container;
    }

    public OptionalSnapshotParticipant<?> updateManager() {
        return updateManager;
    }

    public static class OfFluid extends FabricWrappedContainer<Fluid, FluidResource, FluidVariant> {
        public OfFluid(CommonStorage<FluidResource> container) {
            super(container, ConversionUtils::toVariant, ConversionUtils::toResource);
        }
    }

    public static class OfItem extends FabricWrappedContainer<Item, ItemResource, ItemVariant> {
        public OfItem(CommonStorage<ItemResource> container) {
            super(container, ConversionUtils::toVariant, ConversionUtils::toResource);
        }
    }
}
