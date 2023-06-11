package earth.terrarium.botarium.forge.regsitry.fluid;


import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import net.minecraft.Util;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ForgeFluidAttributesHandler {
    public static ForgeFlowingFluid.Properties propertiesFromFluidProperties(FluidData data){
        FluidAttributes.Builder builder = attributesFromFluidProperties(data.getProperties());
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(() -> data.getStillFluid().get(), () -> data.getFlowingFluid().get(), builder)
                .bucket(() -> data.getBucket().get()).block(() -> data.getBlock().get());
        return properties;
    }

    public static FluidAttributes.Builder attributesFromFluidProperties(FluidProperties fluidProperties){
        FluidAttributes.Builder builder = FluidAttributes.builder(fluidProperties.still(), fluidProperties.flowing())
                .color(fluidProperties.tintColor())
                .density(fluidProperties.density())
                .overlay(fluidProperties.overlay())
                .luminosity(fluidProperties.lightLevel())
                .rarity(fluidProperties.rarity())
                .sound(fluidProperties.sounds().getSound("bucket_fill"), fluidProperties.sounds().getSound("bucket_empty"))
                .temperature(fluidProperties.temperature())
                .translationKey(Util.makeDescriptionId("fluid_type", fluidProperties.id()))
                .viscosity(fluidProperties.viscosity());
        if (fluidProperties.supportsBloating()) builder.gaseous();
        return builder;
    }
}
