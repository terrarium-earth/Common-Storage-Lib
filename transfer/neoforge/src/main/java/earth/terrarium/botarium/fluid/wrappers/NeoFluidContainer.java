package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.CommonStorage;

public record NeoFluidContainer(CommonStorage<FluidResource> container) implements AbstractNeoFluidHandler {
}
