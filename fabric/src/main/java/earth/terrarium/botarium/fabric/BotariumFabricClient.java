package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.api.registry.fluid.FluidProperties;
import earth.terrarium.botarium.fabric.client.BoatriumFluidRenderHandler;
import earth.terrarium.botarium.fabric.registry.fluid.FabricFluidData;
import earth.terrarium.botarium.impl.NewClientApiFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.ArrayList;
import java.util.List;

public class BotariumFabricClient implements ClientModInitializer {

    private static final List<FabricFluidData> FLUIDS_TO_RENDER = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry instance = FluidRenderHandlerRegistry.INSTANCE;
        for (FabricFluidData holder : FLUIDS_TO_RENDER) {
            FluidProperties properties = holder.getProperties();
            ResourceLocation still = properties.still();
            ResourceLocation flowing = properties.flowing();
            ResourceLocation overlay = properties.overlay();

            holder.getOptionalFlowingFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(properties)));
            holder.getOptionalStillFluid().ifPresent(fluid -> instance.register(fluid, new BoatriumFluidRenderHandler(properties)));

            ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register((atlasTexture, registry) -> {
                if (still != null) registry.register(still);
                if (flowing != null) registry.register(flowing);
                if (overlay != null) registry.register(overlay);
            });
        }

        NewClientApiFabric.initialize();
    }

    public static void registerRenderedFluid(FabricFluidData holder) {
        FLUIDS_TO_RENDER.add(holder);
    }
}
