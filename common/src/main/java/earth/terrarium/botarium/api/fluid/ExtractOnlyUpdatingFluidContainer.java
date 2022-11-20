package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.Updatable;

import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class ExtractOnlyUpdatingFluidContainer extends SimpleUpdatingFluidContainer {
    public ExtractOnlyUpdatingFluidContainer(Updatable updatable, IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        super(updatable, maxAmount, tanks, fluidFilter);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }
}
