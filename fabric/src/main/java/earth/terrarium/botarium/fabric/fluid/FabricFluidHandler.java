package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.PlatformFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
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
        while (storage.iterator().hasNext()) {
            size++;
        }
        return size;
    }

    @Override
    public FluidHolder getFluidInTank(int tank) {
        List<FluidHolder> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
        return fluids.get(tank);
    }

    @Override
    public List<FluidHolder> getFluidTanks() {
        List<FluidHolder> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(variant -> fluids.add(FabricFluidHolder.of(variant.getResource(), variant.getAmount())));
        return fluids;
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
