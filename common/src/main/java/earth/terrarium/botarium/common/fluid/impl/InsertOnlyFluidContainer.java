package earth.terrarium.botarium.common.fluid.impl;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.util.Mth;

import java.util.function.BiPredicate;
import java.util.function.IntToLongFunction;

public class InsertOnlyFluidContainer extends SimpleFluidContainer {
    public InsertOnlyFluidContainer(Updatable updatable, IntToLongFunction maxAmount, int tanks, BiPredicate<Integer, FluidHolder> fluidFilter) {
        super(maxAmount, tanks, fluidFilter);
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return FluidHooks.emptyFluid();
    }

    @Override
    public FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        for (int i = 0; i < this.storedFluid.size(); i++) {
            if (fluidFilter.test(i, fluid)) {
                FluidHolder toExtract = fluid.copyHolder();
                if (storedFluid.isEmpty()) {
                    return FluidHooks.emptyFluid();
                } else if (storedFluid.get(i).matches(fluid)) {
                    long extractedAmount = Mth.clamp(fluid.getFluidAmount(), 0, storedFluid.get(i).getFluidAmount());
                    toExtract.setAmount(extractedAmount);
                    if (simulate) return toExtract;
                    this.storedFluid.get(i).setAmount(storedFluid.get(i).getFluidAmount() - extractedAmount);
                    if (storedFluid.get(i).getFluidAmount() == 0) storedFluid.set(i, FluidHooks.emptyFluid());
                    return toExtract;
                }
            }
        }
        return FluidHooks.emptyFluid();
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}
