package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.fabric.client.BoatriumFluidRenderHandler;
import earth.terrarium.botarium.fabric.registry.fluid.FabricFluidData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class BotariumFabricClient implements ClientModInitializer {

    private static final List<FabricFluidData> FLUIDS_TO_RENDER = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry instance = FluidRenderHandlerRegistry.INSTANCE;
        for (FabricFluidData holder : FLUIDS_TO_RENDER) {
            FluidProperties properties = holder.getProperties();

            holder.getOptionalFlowingFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(properties)));
            holder.getOptionalStillFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(properties)));
        }
    }

    public static void registerRenderedFluid(FabricFluidData holder) {
        FLUIDS_TO_RENDER.add(holder);
    }
}
