package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.neoforged.neoforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidUnit unit, long amount) {
        FluidStack stack = new FluidStack(unit.unit(), (int) amount);
        stack.applyComponents(unit.components());
        return stack;
    }

    public static FluidUnit convert(FluidStack stack) {
        return new FluidUnit(stack.getFluid(), stack.getComponentsPatch());
    }
}
