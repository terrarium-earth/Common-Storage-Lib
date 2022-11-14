package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.registry.fluid.FluidData;
import earth.terrarium.botarium.api.registry.fluid.FluidProperties;
import earth.terrarium.botarium.api.registry.fluid.FluidRegistry;
import earth.terrarium.botarium.fabric.BotariumFabricClient;
import earth.terrarium.botarium.fabric.registry.fluid.FabricFluidData;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(FluidRegistry.class)
public class FluidRegistryImpl {

    @ExtensionInjectedElement
    private final String modid;

    @ImplementsBaseElement
    public FluidRegistryImpl(String modid) {
        this.modid = modid;
    }

    @ImplementsBaseElement
    public FluidData register(FluidProperties properties) {
        FabricFluidData holder = new FabricFluidData(properties);
        if (EnvType.CLIENT == FabricLoader.getInstance().getEnvironmentType()) {
            BotariumFabricClient.registerRenderedFluid(holder);
        }
        return holder;
    }

    @ImplementedByExtension
    public FluidData register(String id, FluidProperties.Builder properties) {
        return register(properties.build(new ResourceLocation(modid, id)));
    }

    @ImplementsBaseElement
    public void initialize() {

    }

}
