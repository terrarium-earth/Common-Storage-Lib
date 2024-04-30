package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;

public record NeoFluidContainer(CommonStorage<FluidUnit> container) implements AbstractNeoFluidHandler {
}
