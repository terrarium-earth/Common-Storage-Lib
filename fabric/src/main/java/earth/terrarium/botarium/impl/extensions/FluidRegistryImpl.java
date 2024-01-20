package earth.terrarium.botarium.impl.extensions;

import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.common.registry.fluid.FluidRegistry;
import earth.terrarium.botarium.impl.NewClientApiFabric;
import earth.terrarium.botarium.impl.registry.fluid.FabricFluidData;
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
            NewClientApiFabric.registerRenderedFluid(holder);
        }
        return holder;
    }

    @ImplementedByExtension
    public FluidData register(FluidInformation information) {
        FabricFluidData holder = new FabricFluidData(information);
        if (EnvType.CLIENT == FabricLoader.getInstance().getEnvironmentType()) {
            NewClientApiFabric.registerRenderedFluid(holder);
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
