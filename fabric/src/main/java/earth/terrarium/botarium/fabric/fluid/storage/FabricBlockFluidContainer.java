package earth.terrarium.botarium.fabric.fluid.storage;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ManualSyncing;
import earth.terrarium.botarium.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class FabricBlockFluidContainer<T extends FluidContainer & Updatable> extends ExtendedFluidContainer implements Storage<FluidVariant>, ManualSyncing {
    protected final T container;

    public FabricBlockFluidContainer(T container) {
        this.container = container;
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
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insertFluid(FabricFluidHolder.of(resource, maxAmount), false);
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.extractFluid(FabricFluidHolder.of(resource, maxAmount), false).getFluidAmount();
    }

    @Override
    public @NotNull Iterator<StorageView<FluidVariant>> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < container.getSize();
            }

            @Override
            public StorageView<FluidVariant> next() {
                return new SingleFluidSlot(FabricBlockFluidContainer.this, index++);
            }
        };
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void readSnapshot(FluidSnapshot snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    public void onFinalCommit() {
        container.update();
    }

}
