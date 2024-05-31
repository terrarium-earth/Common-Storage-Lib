package earth.terrarium.common_storage_lib.fluid.util;

import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import net.minecraftforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidResource resource, long amount) {
        FluidStack stack = new FluidStack(resource.getType(), (int) amount);
        stack.setTag(resource.getTag());
        return stack;
    }

    public static FluidResource convert(FluidStack stack) {
        return FluidResource.of(stack.getFluid(), stack.getTag());
    }
}
