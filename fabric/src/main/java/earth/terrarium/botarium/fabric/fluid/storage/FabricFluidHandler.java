package earth.terrarium.botarium.fabric.fluid.storage;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidHandler;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public record FabricFluidHandler(Storage<FluidVariant> storage) implements PlatformFluidHandler {

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long inserted = storage.insert(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long extracted = storage.extract(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted == 0 ? FabricFluidHolder.of(fabricFluidHolder.toVariant(), extracted) : fluid;
        }
    }

    @Override
    public int getTankAmount() {
        int size = 0;
        try(Transaction transaction = Transaction.openOuter()){
            for (StorageView<FluidVariant> ignored : storage.iterable(transaction)) {
                size++;
            }
            transaction.abort();
        }
        return size;
    }

    @Override
    public FluidHolder getFluidInTank(int tank) {
        List<FluidHolder> fluids = new ArrayList<>();
        try(Transaction transaction = Transaction.openOuter()){
            storage.iterator(transaction).forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
            transaction.abort();
        }
        return fluids.get(tank);
    }

    @Override
    public List<FluidHolder> getFluidTanks() {
        List<FluidHolder> fluids = new ArrayList<>();
        try(Transaction transaction = Transaction.openOuter()){
            storage.iterator(transaction).forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
            transaction.abort();
        }
        return fluids;
    }

    @Override
    public long getTankCapacity(int tank) {
        List<StorageView<FluidVariant>> fluids = new ArrayList<>();
        try(Transaction transaction = Transaction.openOuter()) {
            storage.iterator(transaction).forEachRemaining(fluids::add);
            transaction.abort();
        }
        return fluids.get(tank).getCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        return insertFluid(getFluidInTank(0), true) > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return extractFluid(getFluidInTank(0), true).getFluidAmount() > 0;
    }
}
