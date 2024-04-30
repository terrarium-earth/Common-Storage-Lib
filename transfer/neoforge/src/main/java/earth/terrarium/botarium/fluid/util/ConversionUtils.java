package earth.terrarium.botarium.fluid.util;

import earth.terrarium.botarium.fluid.base.FluidUnit;
import net.neoforged.neoforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidUnit unit, long amount) {
        FluidStack stack = new FluidStack(unit.getType(), (int) amount);
        stack.applyComponents(unit.components());
        return stack;
    }

    public static FluidUnit convert(FluidStack stack) {
        return new FluidUnit(stack.getFluid(), stack.getComponentsPatch());
    }
}
