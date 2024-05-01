package earth.terrarium.botarium.fluid.util;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import net.neoforged.neoforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidResource unit, long amount) {
        FluidStack stack = new FluidStack(unit.getType(), (int) amount);
        stack.applyComponents(unit.components());
        return stack;
    }

    public static FluidResource convert(FluidStack stack) {
        return new FluidResource(stack.getFluid(), stack.getComponentsPatch());
    }
}
