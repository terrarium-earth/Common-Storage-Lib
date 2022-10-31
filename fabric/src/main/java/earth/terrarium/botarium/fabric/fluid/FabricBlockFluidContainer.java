package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import earth.terrarium.botarium.api.fluid.UpdatingFluidContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockFluidContainer extends ExtendedFluidContainer implements Storage<FluidVariant>, ManualSyncing {
    private final UpdatingFluidContainer container;

    public FabricBlockFluidContainer(UpdatingFluidContainer container) {
        this.container = container;
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
    public Iterator<StorageView<FluidVariant>> iterator() {
        return container.getFluids().stream().map(holder -> new WrappedFluidHolder(this, holder, container::extractFromSlot)).map(holder -> (StorageView<FluidVariant>) holder).iterator();
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
