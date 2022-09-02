package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockFluidContainer extends SnapshotParticipant<FluidContainer> implements Storage<FluidVariant> {
    private final FluidContainer container;

    public FabricBlockFluidContainer(FluidContainer container) {
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
        return container.getFluids().stream().map(holder -> new WrappedFluidHolder(holder, container::extractFromSlot)).map(holder -> (StorageView<FluidVariant>) holder).iterator();
    }

    @Override
    protected FluidContainer createSnapshot() {
        return container.copy();
    }

    @Override
    protected void readSnapshot(FluidContainer snapshot) {
        container.fromContainer(snapshot);
    }
}
