package earth.terrarium.botarium.impl;

import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.impl.client.BoatriumFluidRenderHandler;
import earth.terrarium.botarium.impl.registry.fluid.FabricFluidData;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class NewClientApiFabric {

    private static final List<FabricFluidData> FLUIDS_TO_RENDER = new ArrayList<>();

    public static void initialize() {
        FluidRenderHandlerRegistry instance = FluidRenderHandlerRegistry.INSTANCE;
        for (FabricFluidData holder : FLUIDS_TO_RENDER) {
            FluidInformation info = holder.getInformation();

            holder.getOptionalFlowingFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(info)));
            holder.getOptionalStillFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(info)));
        }
    }

    public static void registerRenderedFluid(FabricFluidData holder) {
        FLUIDS_TO_RENDER.add(holder);
    }
}
