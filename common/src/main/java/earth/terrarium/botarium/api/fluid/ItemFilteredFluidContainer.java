package earth.terrarium.botarium.api.fluid;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class ItemFilteredFluidContainer implements ItemFluidContainer {
    private final NonNullList<FluidHolder> storedFluid;
    private final BiPredicate<Integer, FluidHolder> fluidFilter;
    private final long maxAmount;
    private final ItemStack containerItem;

    public ItemFilteredFluidContainer(long maxAmount, int size, ItemStack itemStack, BiPredicate<Integer, FluidHolder> fluidFilter) {
        this.maxAmount = maxAmount;
        this.fluidFilter = fluidFilter;
        this.storedFluid = NonNullList.withSize(size, FluidHooks.emptyFluid());
        this.containerItem = itemStack;
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

    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        if (Objects.equals(fluidHolder.getCompound(), toInsert.getCompound()) && fluidHolder.getFluid().isSame(toInsert.getFluid())) {
            long extracted = Mth.clamp(toInsert.getFluidAmount(), 0, fluidHolder.getFluidAmount());
            snapshot.run();
            fluidHolder.setAmount(fluidHolder.getFluidAmount() - extracted);
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

    @Override
    public ItemFilteredFluidContainer copy() {
        return new ItemFilteredFluidContainer(maxAmount, this.getSize(), containerItem.copy(), fluidFilter);
    }

    @Override
    public long maxStackSize() {
        return this.maxAmount;
    }

    @Override
    public void fromContainer(FluidContainer container) {
        for (int i = 0; i < container.getSize(); i++) {
            this.storedFluid.set(i, container.getFluids().get(i).copyHolder());
        }
    }

    @Override
    public void deserialize(CompoundTag tag) {
        ListTag fluids = tag.getList("StoredFluids", Tag.TAG_COMPOUND);
        for (int i = 0; i < fluids.size(); i++) {
            CompoundTag fluid = fluids.getCompound(i);
            this.storedFluid.set(i, FluidHooks.fluidFromCompound(fluid));
        }
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
    public ItemStack getContainerItem() {
        return containerItem;
    }
}
