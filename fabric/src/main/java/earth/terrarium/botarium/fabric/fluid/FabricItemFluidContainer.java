package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class FabricItemFluidContainer extends SnapshotParticipant<FluidSnapshot> implements Storage<FluidVariant> {
    private final FluidContainer container;
    private final ContainerItemContext ctx;

    public FabricItemFluidContainer(ContainerItemContext ctx, FluidContainer container) {
        this.container = container;
        this.ctx = ctx;
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long inserted = container.insertFluid(FabricFluidHolder.of(resource, maxAmount), false);
        setChanged(transaction);
        return inserted;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long extracted = container.extractFluid(FabricFluidHolder.of(resource, maxAmount), false).getFluidAmount();
        setChanged(transaction);
        return extracted;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return container.getFluids().stream().map(holder -> new WrappedFluidHolder(holder, container::extractFromSlot)).map(holder -> (StorageView<FluidVariant>) holder).iterator();
    }

    @Override
    protected FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(FluidSnapshot snapshot) {
        container.readSnapshot(snapshot);
    }

    public void setChanged(TransactionContext transaction) {
        ItemStack stack = ctx.getItemVariant().toStack();
        this.updateSnapshots(transaction);
        this.container.serialize(stack.getOrCreateTag());
        ctx.exchange(ItemVariant.of(stack), ctx.getAmount(), transaction);
    }
}
