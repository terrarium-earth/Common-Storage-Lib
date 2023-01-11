package earth.terrarium.botarium.common.fluid.impl;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record WrappedItemFluidContainer(ItemStack stack, FluidContainer container) implements ItemFluidContainer, Updatable<ItemStack> {

    public WrappedItemFluidContainer {
        container.deserialize(stack.getOrCreateTag());
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return container.insertFluid(fluid, simulate);
    }

    @Override
    public long internalInsert(FluidHolder fluids, boolean simulate) {
        long inserted = container.internalInsert(fluids, simulate);
        if (!simulate) update(stack);
        return inserted;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return container.extractFluid(fluid, simulate);
    }

    @Override
    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        FluidHolder extracted = container.internalExtract(fluid, simulate);
        if (!simulate) update(stack);
        return extracted;
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        container.setFluid(slot, fluid);
        update(stack);
    }

    @Override
    public List<FluidHolder> getFluids() {
        return container.getFluids();
    }

    @Override
    public int getSize() {
        return container.getSize();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public FluidContainer copy() {
        return container.copy();
    }

    @Override
    public long getTankCapacity(int tankSlot) {
        return container.getTankCapacity(tankSlot);
    }

    @Override
    public void fromContainer(FluidContainer container) {
        this.container.fromContainer(container);
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        return container.extractFromSlot(fluidHolder, toInsert, snapshot);
    }

    @Override
    public boolean allowsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public ItemStack getContainerItem() {
        return stack;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        container.deserialize(nbt);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void update(ItemStack object) {
        serialize(object.getOrCreateTag());
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
