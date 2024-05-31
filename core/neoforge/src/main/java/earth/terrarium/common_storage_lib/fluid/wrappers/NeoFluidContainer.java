package earth.terrarium.common_storage_lib.fluid.wrappers;

import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;

public record NeoFluidContainer(CommonStorage<FluidResource> container) implements AbstractNeoFluidHandler {
}
