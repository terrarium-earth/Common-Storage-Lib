package earth.terrarium.botarium.api.fluid;

import net.minecraft.nbt.CompoundTag;

import java.util.List;

public interface FluidContainer {
    long insertFluid(FluidHolder fluid, boolean simulate);
    FluidHolder extractFluid(FluidHolder fluid, boolean simulate);

    void setFluid(int slot, FluidHolder fluid);
    List<FluidHolder> getFluids();
    int getSize();
    boolean isEmpty();
    FluidContainer copy();
    long maxStackSize();
    void fromContainer(FluidContainer container);

    long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot);

    CompoundTag serialize(CompoundTag tag);
    void deseralize(CompoundTag tag);

    boolean allowsInsertion();
    boolean allowsExtraction();
}
