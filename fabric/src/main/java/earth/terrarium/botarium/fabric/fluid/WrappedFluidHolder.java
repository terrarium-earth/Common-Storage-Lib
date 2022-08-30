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

    public WrappedFluidHolder(FluidHolder fluidHolder) {
        this.fluidHolder = fluidHolder;
    }

    public FluidVariant fluidVariant() {
        return FluidVariant.of(fluidHolder.getFluid(), fluidHolder.getCompound());
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (fluidVariant().nbtMatches(resource.getNbt()) && fluidVariant().isOf(resource.getFluid())) {
            long extracted = Mth.clamp(maxAmount, 0, this.getAmount());
            this.updateSnapshots(transaction);
            this.fluidHolder.setAmount(this.fluidHolder.getFluidAmount() - extracted);
            return extracted;
        }
        return 0;
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
}
