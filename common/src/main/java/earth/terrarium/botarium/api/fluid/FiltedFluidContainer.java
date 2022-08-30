package earth.terrarium.botarium.api.fluid;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class FiltedFluidContainer implements FluidContainer {
    NonNullList<FluidHolder> storedFluid;
    long maxAmount;
    BiPredicate<Integer, FluidHolder> fluidFilter;

    public FiltedFluidContainer(long maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this.maxAmount = maxAmount;
        this.fluidFilter = fluidFilter;
        this.storedFluid = NonNullList.withSize(tanks, FluidHooks.emptyFluid());
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if(fluidFilter.test(i, fluid)) {
                if(storedFluid.get(i).isEmpty()) {
                    FluidHolder insertedFluid = fluid.copyHolder();
                    insertedFluid.setAmount(Mth.clamp(fluid.getFluidAmount(), 0, maxAmount));
                    if(simulate) return insertedFluid.getFluidAmount();
                    this.storedFluid.set(i, insertedFluid);
                    return storedFluid.get(i).getFluidAmount();
                } else {
                    if (storedFluid.get(i).matches(fluid)) {
                        long insertedAmount = Mth.clamp(fluid.getFluidAmount(), 0, maxAmount - storedFluid.get(i).getFluidAmount());
                        if(simulate) return insertedAmount;
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
            if(fluidFilter.test(i, fluid)) {
                FluidHolder toExtract = fluid.copyHolder();
                if(storedFluid.isEmpty()) {
                    return FluidHooks.emptyFluid();
                } else {
                    if (storedFluid.get(i).matches(fluid)) {
                        long extractedAmount = Mth.clamp(fluid.getFluidAmount(), 0, storedFluid.get(i).getFluidAmount());
                        toExtract.setAmount(extractedAmount);
                        if(simulate) return toExtract;
                        this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() - extractedAmount);
                        if(storedFluid.get(i).getFluidAmount() == 0) storedFluid.set(i, FluidHooks.emptyFluid());
                        return toExtract;
                    }
                }
            }
        }
        return FluidHooks.emptyFluid();
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

    @Override
    public FluidContainer copy() {
        return new FiltedFluidContainer(maxAmount, this.getSize(), fluidFilter);
    }

    @Override
    public void fromContainer(FluidContainer container) {

    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        ListTag tags = new ListTag();
        for (FluidHolder fluidHolder : this.storedFluid) {
            CompoundTag fluid = new CompoundTag();
            fluid.put("StoredFluid", fluidHolder.serialize());
            tags.add(fluid);
        }
        tag.put("StoredFluids", tags);
        return tag;
    }

    @Override
    public void deseralize(CompoundTag tag) {
        ListTag fluids = tag.getList("StoredFluids", Tag.TAG_COMPOUND);
        for (int i = 0; i < fluids.size(); i++) {
            CompoundTag fluid = fluids.getCompound(i);
            this.storedFluid.set(i, FluidHooks.fluidFromCompound(fluid));
        }
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }
}
