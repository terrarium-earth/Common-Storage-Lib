package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.common.registry.fluid.FluidRegistry;
import earth.terrarium.botarium.forge.regsitry.fluid.BotariumFluidType;
import earth.terrarium.botarium.forge.regsitry.fluid.ForgeFluidData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(FluidRegistry.class)
public class FluidRegistryImpl {

    @ExtensionInjectedElement
    private final DeferredRegister<FluidType> registry;

    @ExtensionInjectedElement
    private final String modid;

    @ImplementsBaseElement
    public FluidRegistryImpl(String modid) {
        this.registry = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modid);
        this.modid = modid;
    }

    @ImplementsBaseElement
    public FluidData register(FluidProperties properties) {
        RegistryObject<BotariumFluidType> type = registry.register(properties.id().getPath(), () -> BotariumFluidType.of(properties));
        return new ForgeFluidData(type, properties);
    }

    @ImplementedByExtension
    public FluidData register(FluidInformation information) {
        RegistryObject<BotariumFluidType> type = registry.register(information.id().getPath(), () -> BotariumFluidType.create(information));
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
