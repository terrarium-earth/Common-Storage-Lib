package earth.terrarium.botarium.common.fluid.impl;

import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class SimpleFluidContainer implements FluidContainer {
    public static final String FLUID_KEY = "StoredFluids";

    public NonNullList<FluidHolder> storedFluid;
    public final IntToLongFunction maxAmount;
    public final BiPredicate<Integer, FluidHolder> fluidFilter;

    public SimpleFluidContainer(IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this.maxAmount = maxAmount;
        this.fluidFilter = fluidFilter;
        this.storedFluid = NonNullList.withSize(tanks, FluidHooks.emptyFluid());
    }

    public SimpleFluidContainer(long maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this(integer -> maxAmount, tanks, fluidFilter);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if (fluidFilter.test(i, fluid)) {
                if (storedFluid.get(i).isEmpty()) {
                    FluidHolder insertedFluid = fluid.copyHolder();
                    insertedFluid.setAmount(Mth.clamp(fluid.getFluidAmount(), 0, maxAmount.applyAsLong(i)));
                    if (simulate) return insertedFluid.getFluidAmount();
                    this.storedFluid.set(i, insertedFluid);
                    return storedFluid.get(i).getFluidAmount();
                } else {
                    if (storedFluid.get(i).matches(fluid)) {
                        long insertedAmount = Mth.clamp(fluid.getFluidAmount(), 0, maxAmount.applyAsLong(i) - storedFluid.get(i).getFluidAmount());
                        if (simulate) return insertedAmount;
                        this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() + insertedAmount);
                        return insertedAmount;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if (fluidFilter.test(i, fluid)) {
                FluidHolder toExtract = fluid.copyHolder();
                if (storedFluid.isEmpty()) {
                    return FluidHooks.emptyFluid();
                } else if (storedFluid.get(i).matches(fluid)) {
                    long extractedAmount = Mth.clamp(fluid.getFluidAmount(), 0, storedFluid.get(i).getFluidAmount());
                    toExtract.setAmount(extractedAmount);
                    if (simulate) return toExtract;
                    this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() - extractedAmount);
                    if (storedFluid.get(i).getFluidAmount() == 0) storedFluid.set(i, FluidHooks.emptyFluid());
                    return toExtract;
                }
            }
        }
        return FluidHooks.emptyFluid();
    }

    public long internalInsert(FluidHolder fluid, boolean simulate) {
        return insertFluid(fluid, simulate);
    }

    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        return extractFluid(fluid, simulate);
    }

    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        if (Objects.equals(fluidHolder.getCompound(), toInsert.getCompound()) && fluidHolder.getFluid().isSame(toInsert.getFluid())) {
            long extracted = Mth.clamp(toInsert.getFluidAmount(), 0, fluidHolder.getFluidAmount());
            snapshot.run();
            fluidHolder.setAmount(fluidHolder.getFluidAmount() - extracted);
            if(fluidHolder.getFluidAmount() == 0) fluidHolder.setFluid(Fluids.EMPTY);
            return extracted;
        }
        return 0;
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        this.storedFluid.set(slot, fluid);
    }

    @Override
    public List<FluidHolder> getFluids() {
        return storedFluid;
    }

    @Override
    public int getSize() {
        return getFluids().size();
    }

    @Override
    public boolean isEmpty() {
        return getFluids().isEmpty() || getFluids().get(0) == null || getFluids().get(0).isEmpty();
    }

    /**
     * Clears the container of all fluids.
     * <br />
     * <u>Meant only for internal use!</u>
     */
    public void clear() {
        this.storedFluid.clear();
    }

    @Override
    public SimpleFluidContainer copy() {
        return new SimpleFluidContainer(maxAmount, this.getSize(), fluidFilter);
    }

    @Override
    public long getTankCapacity(int slot) {
        return this.maxAmount.applyAsLong(slot);
    }

    @Override
    public void fromContainer(FluidContainer container) {
        this.storedFluid = NonNullList.withSize(container.getSize(), FluidHooks.emptyFluid());
        for (int i = 0; i < container.getSize(); i++) {
            this.storedFluid.set(i, container.getFluids().get(i).copyHolder());
        }
    }

    @Override
    public void deserialize(CompoundTag tag) {
        ListTag fluids = tag.getList(FLUID_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < fluids.size(); i++) {
            CompoundTag fluid = fluids.getCompound(i);
            this.storedFluid.set(i, FluidHooks.fluidFromCompound(fluid));
        }
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        if(!this.storedFluid.isEmpty()) {
            ListTag tags = new ListTag();
            for (FluidHolder fluidHolder : this.storedFluid) {
                tags.add(fluidHolder.serialize());
            }
            tag.put(FLUID_KEY, tags);
        }
        return tag;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
    }

    @Override
    public void clearContent() {
        this.storedFluid.clear();
    }
}