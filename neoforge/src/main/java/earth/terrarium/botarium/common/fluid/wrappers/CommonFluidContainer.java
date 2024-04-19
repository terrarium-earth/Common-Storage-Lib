package earth.terrarium.botarium.common.fluid.wrappers;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record CommonFluidContainer(IFluidHandler handler) implements AbstractCommonFluidContainer {
}
