package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.Updatable;

public interface FluidHoldingBlock extends Updatable {
    UpdatingFluidContainer getFluidContainer();
}
