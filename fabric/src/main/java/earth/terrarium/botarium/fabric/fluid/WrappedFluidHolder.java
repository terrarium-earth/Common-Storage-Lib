package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.util.Mth;

@SuppressWarnings("UnstableApiUsage")
public class WrappedFluidHolder extends SnapshotParticipant<FluidHolder> implements StorageView<FluidVariant> {

    private final FluidHolder fluidHolder;
    private final FluidExtraction extraction;

    public WrappedFluidHolder(FluidHolder fluidHolder, FluidExtraction extraction) {
        this.fluidHolder = fluidHolder;
        this.extraction = extraction;
    }

    public FluidVariant fluidVariant() {
        return FluidVariant.of(fluidHolder.getFluid(), fluidHolder.getCompound());
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return extraction.apply(this.fluidHolder, FabricFluidHolder.of(resource, maxAmount), () -> this.updateSnapshots(transaction));
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
        fluidHolder.setFluid(snapshot.getFluid());
        fluidHolder.setAmount(snapshot.getFluidAmount());
        fluidHolder.setCompound(snapshot.getCompound());
    }

    @FunctionalInterface
    public interface FluidExtraction {
        long apply(FluidHolder fluidHolder, FluidHolder toInsert, Runnable runnable);
    }
}
