package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.common.item.ItemStackHolder;

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
