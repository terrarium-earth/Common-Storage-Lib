package earth.terrarium.common_storage_lib.fluid.wrappers;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record CommonFluidContainer(IFluidHandler handler) implements AbstractCommonFluidContainer {
}
