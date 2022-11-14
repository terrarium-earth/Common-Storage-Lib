package earth.terrarium.botarium.forge.regsitry.fluid;

import earth.terrarium.botarium.api.registry.fluid.FluidProperties;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BotariumFluidType extends FluidType {

    private final FluidProperties fluidProperties;

    private BotariumFluidType(FluidProperties fluidProperties, Properties properties) {
        super(properties);
        this.fluidProperties = fluidProperties;
    }

    public static BotariumFluidType of(FluidProperties fluidProperties) {
        var properties = Properties.create();
        properties.descriptionId(Util.makeDescriptionId("fluid_type", fluidProperties.id()));
        properties.adjacentPathType(fluidProperties.adjacentPathType());
        properties.canConvertToSource(fluidProperties.canConvertToSource());
        properties.canDrown(fluidProperties.canDrown());
        properties.canExtinguish(fluidProperties.canExtinguish());
        properties.canHydrate(fluidProperties.canHydrate());
        properties.canPushEntity(fluidProperties.canPushEntity());
        properties.canSwim(fluidProperties.canSwim());
        properties.density(fluidProperties.density());
        properties.fallDistanceModifier(fluidProperties.fallDistanceModifier());
        properties.lightLevel(fluidProperties.lightLevel());
        properties.motionScale(fluidProperties.motionScale());
        properties.pathType(fluidProperties.pathType());
        properties.rarity(fluidProperties.rarity());
        properties.temperature(fluidProperties.temperature());
        properties.viscosity(fluidProperties.viscosity());
        fluidProperties.sounds().getSounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound));
        return new BotariumFluidType(fluidProperties, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        var type = this;
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return type.fluidProperties.still();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return type.fluidProperties.flowing();
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return type.fluidProperties.overlay();
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return type.fluidProperties.screenOverlay();
            }

            @Override
            public int getTintColor() {
                return type.fluidProperties.tintColor();
            }
        });
    }
}
