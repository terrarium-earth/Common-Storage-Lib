package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.item.ItemStackHolder;

import java.util.List;

public interface PlatformFluidItemHandler {

    long insertFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate);

    FluidHolder extractFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate);

    int getTankAmount();

    FluidHolder getFluidInTank(int tank);

    long getTankCapacity(int tank);

    /**
     * @return If the handler supports insertion.
     */
    boolean supportsInsertion();

    /**
     * @return If the handler supports extraction.
     */
    boolean supportsExtraction();
}
