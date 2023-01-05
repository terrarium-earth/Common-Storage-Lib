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
        if (stack.hasTag()) container.deserialize(stack.getTag());
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        long insert = container.insertFluid(fluid, simulate);
        if (!simulate) update(stack);
        return insert;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        FluidHolder extract = container.extractFluid(fluid, simulate);
        if (!simulate) update(stack);
        return extract;
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
        update(stack);
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        long extract = container.extractFromSlot(fluidHolder, toInsert, snapshot);
        update(stack);
        return extract;
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
