package earth.terrarium.botarium.fluid.util;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import net.minecraftforge.fluids.FluidStack;

public class ConversionUtils {
    public static FluidStack convert(FluidResource resource, long amount) {
        FluidStack stack = new FluidStack(resource.getType(), (int) amount);
        stack.applyComponents(resource.getDataPatch());
        return stack;
    }

    public static FluidResource convert(FluidStack stack) {
        return FluidResource.of(stack.getFluid(), stack.getComponentsPatch());
    }
}
