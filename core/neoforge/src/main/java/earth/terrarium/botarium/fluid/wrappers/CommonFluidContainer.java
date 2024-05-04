package earth.terrarium.botarium.fluid.wrappers;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record CommonFluidContainer(IFluidHandler handler) implements AbstractCommonFluidContainer {
}
