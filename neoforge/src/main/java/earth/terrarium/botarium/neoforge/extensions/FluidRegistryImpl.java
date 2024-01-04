package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.common.registry.fluid.FluidRegistry;
import earth.terrarium.botarium.neoforge.regsitry.fluid.BotariumFluidType;
import earth.terrarium.botarium.neoforge.regsitry.fluid.ForgeFluidData;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@ClassExtension(FluidRegistry.class)
public class FluidRegistryImpl {

    @ExtensionInjectedElement
    private final DeferredRegister<FluidType> registry;

    @ExtensionInjectedElement
    private final String modid;

    @ImplementsBaseElement
    public FluidRegistryImpl(String modid) {
        this.registry = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modid);
        this.modid = modid;
    }

    @ImplementsBaseElement
    public FluidData register(FluidProperties properties) {
        DeferredHolder<FluidType, BotariumFluidType> type = registry.register(properties.id().getPath(), () -> BotariumFluidType.of(properties));
        return new ForgeFluidData(type, properties);
    }

    @ImplementedByExtension
    public FluidData register(FluidInformation information) {
        DeferredHolder<FluidType, BotariumFluidType> type = registry.register(information.id().getPath(), () -> BotariumFluidType.create(information));
        return new ForgeFluidData(type, information);
    }

    @ImplementedByExtension
    public FluidData register(String id, FluidProperties.Builder properties) {
        return register(properties.build(new ResourceLocation(modid, id)));
    }

    @ImplementsBaseElement
    public void initialize() {
        registry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
