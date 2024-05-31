package earth.terrarium.botarium.fluid.wrappers;

import net.minecraftforge.fluids.capability.IFluidHandler;

public record CommonFluidContainer(IFluidHandler handler) implements AbstractCommonFluidContainer {
}
