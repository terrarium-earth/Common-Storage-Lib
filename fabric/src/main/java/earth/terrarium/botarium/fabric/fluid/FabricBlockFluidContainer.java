package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import earth.terrarium.botarium.api.fluid.UpdatingFluidContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockFluidContainer extends ExtendedFluidContainer implements Storage<FluidVariant>, ManualSyncing {
    private final UpdatingFluidContainer<BlockEntity> container;
    private final BlockEntity block;

    public FabricBlockFluidContainer(UpdatingFluidContainer<BlockEntity> container, BlockEntity entity) {
        this.container = container;
        this.block = entity;
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
        List<FluidHolder> fluids = container.getFluids();
        return IntStream.range(0, fluids.size()).mapToObj(index -> new WrappedFluidHolder(this, fluids.get(index), container::extractFromSlot, container.getTankCapacity(index))).map(holder -> (StorageView<FluidVariant>) holder).iterator();
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
        container.update(block);
    }

}
