package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.Serializable;
import net.minecraft.core.Direction;

import java.util.List;

public interface FluidContainer extends Serializable {
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

    boolean allowsInsertion();
    boolean allowsExtraction();

    default FluidContainer getContainer(Direction direction) {
        return this;
    }
}
