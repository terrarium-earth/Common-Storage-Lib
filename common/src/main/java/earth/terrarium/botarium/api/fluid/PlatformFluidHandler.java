package earth.terrarium.botarium.api.fluid;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PlatformFluidHandler {
    long insertFluid(FluidHolder fluid, boolean simulate);
    FluidHolder extractFluid(FluidHolder fluid, boolean simulate);
    int getTankAmount();
    FluidHolder getFluidInTank(int tank);
}
