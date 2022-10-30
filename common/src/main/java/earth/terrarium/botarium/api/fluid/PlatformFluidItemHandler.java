package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.item.ItemStackHolder;

public interface PlatformFluidItemHandler {

    long insertFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate);

    FluidHolder extractFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate);

    int getTankAmount();

    FluidHolder getFluidInTank(int tank);

    /**
     * @return If the handler supports insertion.
     */
    boolean supportsInsertion();

    /**
     * @return If the handler supports extraction.
     */
    boolean supportsExtraction();
}
