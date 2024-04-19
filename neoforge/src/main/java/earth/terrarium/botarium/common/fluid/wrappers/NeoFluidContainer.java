package earth.terrarium.botarium.common.fluid.wrappers;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;

public record NeoFluidContainer(UnitContainer<FluidUnit> container) implements AbstractNeoFluidHandler {
}
