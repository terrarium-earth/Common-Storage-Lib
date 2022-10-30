package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class WrappedFluidHolder extends SnapshotParticipant<FluidHolder> implements StorageView<FluidVariant> {

    private FluidHolder fluidHolder;
    private final FluidExtraction extraction;
    private final @Nullable ManualSyncing container;

    public WrappedFluidHolder(@Nullable ManualSyncing container, FluidHolder fluidHolder, FluidExtraction extraction) {
        this.fluidHolder = fluidHolder;
        this.extraction = extraction;
        this.container = container;
    }

    public FluidVariant fluidVariant() {
        return FluidVariant.of(fluidHolder.getFluid(), fluidHolder.getCompound());
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long apply = extraction.apply(this.fluidHolder, FabricFluidHolder.of(resource, maxAmount), () -> this.updateSnapshots(transaction));
        container.setChanged(transaction);
        return apply;
    }

    @Override
    public boolean isResourceBlank() {
        return fluidVariant().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return fluidVariant();
    }

    @Override
    public long getAmount() {
        return fluidHolder.getFluidAmount();
    }

    @Override
    public long getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected FluidHolder createSnapshot() {
        return fluidHolder.copyHolder();
    }

    @Override
    protected void readSnapshot(FluidHolder snapshot) {
        fluidHolder = snapshot.copyHolder();
    }

    @Override
    protected void onFinalCommit() {
        if(container != null) {
            container.finalChange();
        }
    }

    @FunctionalInterface
    public interface FluidExtraction {
        long apply(FluidHolder fluidHolder, FluidHolder toInsert, Runnable runnable);
    }
}
