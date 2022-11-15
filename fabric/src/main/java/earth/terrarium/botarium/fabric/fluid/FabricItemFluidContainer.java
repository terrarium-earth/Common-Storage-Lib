package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class FabricItemFluidContainer extends ExtendedFluidContainer implements Storage<FluidVariant>, ManualSyncing {
    private final FluidContainer container;
    private final ContainerItemContext ctx;

    public FabricItemFluidContainer(ContainerItemContext ctx, FluidContainer container) {
        this.container = container;
        this.ctx = ctx;
        CompoundTag nbt = ctx.getItemVariant().getNbt();
        if(nbt != null) container.deserialize(nbt);
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long inserted = container.insertFluid(FabricFluidHolder.of(resource, maxAmount), false);
        setChanged(transaction);
        return inserted;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        long extracted = container.extractFluid(FabricFluidHolder.of(resource, maxAmount), false).getFluidAmount();
        setChanged(transaction);
        return extracted;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return container.getFluids().stream().map(holder -> new WrappedFluidHolder(this, holder, container::extractFromSlot)).map(holder -> (StorageView<FluidVariant>) holder).iterator();
    }

    @Override
    public void onFinalCommit() {
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
    public void setChanged(TransactionContext transaction) {
        ItemStack stack = ctx.getItemVariant().toStack();
        this.container.serialize(stack.getOrCreateTag());
        ctx.exchange(ItemVariant.of(stack), ctx.getAmount(), transaction);
    }
}
