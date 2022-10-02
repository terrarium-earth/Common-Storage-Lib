package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.Updatable;

public interface FluidHoldingBlock extends Updatable {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    UpdatingFluidContainer getFluidContainer();
}
