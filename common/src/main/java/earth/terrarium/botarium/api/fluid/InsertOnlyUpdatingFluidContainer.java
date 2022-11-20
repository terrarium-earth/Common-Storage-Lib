package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.Updatable;

import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class InsertOnlyUpdatingFluidContainer extends SimpleUpdatingFluidContainer {
    public InsertOnlyUpdatingFluidContainer(Updatable updatable, IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        super(updatable, maxAmount, tanks, fluidFilter);
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return FluidHooks.emptyFluid();
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}
