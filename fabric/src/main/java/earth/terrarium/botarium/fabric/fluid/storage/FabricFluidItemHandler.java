package earth.terrarium.botarium.fabric.fluid.storage;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidItemHandler;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public record FabricFluidItemHandler(ItemStack stack, ContainerItemContext context,
                                     Storage<FluidVariant> storage) implements PlatformFluidItemHandler {

    public FabricFluidItemHandler(ItemStack stack) {
        this(stack, ItemStackStorage.of(stack));
    }

    public FabricFluidItemHandler(ItemStack stack, ContainerItemContext context) {
        this(stack, context, FluidStorage.ITEM.find(stack, context));
    }

    @Override
    public long insertFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long inserted = storage.insert(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
                item.setStack(context.getItemVariant().toStack());
            }
            return inserted;
        }
    }

    @Override
    public FluidHolder extractFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            FabricFluidHolder fabricFluidHolder = FabricFluidHolder.of(fluid);
            long extracted = storage.extract(fabricFluidHolder.toVariant(), fabricFluidHolder.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
                item.setStack(context.getItemVariant().toStack());
            }
            return extracted == 0 ? FluidHooks.emptyFluid() : FabricFluidHolder.of(fabricFluidHolder.toVariant(), extracted);
        }
    }

    @Override
    public int getTankAmount() {
        int size = 0;
        for (StorageView<FluidVariant> ignored : storage) {
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
    public long getTankCapacity(int tank) {
        List<StorageView<FluidVariant>> fluids = new ArrayList<>();
        storage.iterator().forEachRemaining(fluids::add);
        return fluids.get(tank).getCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        return insertFluid(new ItemStackHolder(stack).copy(), getFluidInTank(0), true) > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return extractFluid(new ItemStackHolder(stack), getFluidInTank(0), true).getFluidAmount() > 0;
    }
}
