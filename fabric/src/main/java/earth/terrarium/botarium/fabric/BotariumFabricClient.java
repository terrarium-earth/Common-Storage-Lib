package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.client.FluidHolderParticle;
import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.fabric.client.BoatriumFluidRenderHandler;
import earth.terrarium.botarium.fabric.registry.fluid.FabricFluidData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class BotariumFabricClient implements ClientModInitializer {

    private static final List<FabricFluidData> FLUIDS_TO_RENDER = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry instance = FluidRenderHandlerRegistry.INSTANCE;
        for (FabricFluidData holder : FLUIDS_TO_RENDER) {
            FluidInformation info = holder.getInformation();

            holder.getOptionalFlowingFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(info)));
            holder.getOptionalStillFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(info)));
        }

        ParticleFactoryRegistry.getInstance().register(Botarium.FLUID_PARTICLE.get(), (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> new FluidHolderParticle(level, type.fluid(), x, y, z, xSpeed, ySpeed, zSpeed));
    }

    public static void registerRenderedFluid(FabricFluidData holder) {
        FLUIDS_TO_RENDER.add(holder);
    }
}
