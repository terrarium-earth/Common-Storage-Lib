package earth.terrarium.botarium.impl.fluid.storage;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import earth.terrarium.botarium.impl.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class SingleItemFluidSlot extends ExtendedFluidContainer implements StorageView<FluidVariant> {

    private final FabricItemFluidContainer container;
    private final int slotIndex;

    public SingleItemFluidSlot(FabricItemFluidContainer container, int slotIndex) {
        this.container = container;
        this.slotIndex = slotIndex;
    }

    public FluidVariant fluidVariant() {
        FluidHolder fluidHolder = container.container.getFluids().get(slotIndex);
        return FluidVariant.of(fluidHolder.getFluid(), fluidHolder.getCompound());
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long apply = container.container.extractFromSlot(slotIndex, FabricFluidHolder.of(resource, maxAmount), false);
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
        return container.container.getFluids().get(slotIndex).getFluidAmount();
    }

    @Override
    public long getCapacity() {
        return container.container.getTankCapacity(slotIndex);
    }

    @Override
    public void onFinalCommit() {
        container.onFinalCommit();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void readSnapshot(FluidSnapshot snapshot) {
        container.readSnapshot(snapshot);
    }
}