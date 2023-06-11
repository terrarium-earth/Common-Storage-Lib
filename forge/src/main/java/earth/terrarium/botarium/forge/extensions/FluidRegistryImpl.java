package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.common.registry.fluid.FluidRegistry;
import earth.terrarium.botarium.forge.regsitry.fluid.ForgeFluidData;
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
        return new ForgeFluidData(properties);
    }

    @ImplementedByExtension
    public FluidData register(String id, FluidProperties.Builder properties) {
        return register(properties.build(new ResourceLocation(modid, id)));
    }

    @ImplementsBaseElement
    public void initialize() {
    }

}
