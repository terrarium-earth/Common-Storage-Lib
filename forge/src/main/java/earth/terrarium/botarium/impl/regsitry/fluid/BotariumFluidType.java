package earth.terrarium.botarium.impl.regsitry.fluid;

import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BotariumFluidType extends FluidType {

    private final FluidInformation information;

    private BotariumFluidType(FluidInformation information, Properties properties) {
        super(properties);
        this.information = information;
    }

    public static BotariumFluidType create(FluidInformation information) {
        var properties = Properties.create();
        properties.descriptionId(Util.makeDescriptionId("fluid_type", information.id()));
        properties.adjacentPathType(information.adjacentPathType());
        properties.canConvertToSource(information.canConvertToSource());
        properties.canDrown(information.canDrown());
        properties.canExtinguish(information.canExtinguish());
        properties.canHydrate(information.canHydrate());
        properties.canPushEntity(information.canPushEntity());
        properties.canSwim(information.canSwim());
        properties.density(information.density());
        properties.fallDistanceModifier(information.fallDistanceModifier());
        properties.lightLevel(information.lightLevel());
        properties.motionScale(information.motionScale());
        properties.pathType(information.pathType());
        properties.rarity(information.rarity());
        properties.temperature(information.temperature());
        properties.viscosity(information.viscosity());
        information.sounds().getSounds().forEach((name, sound) -> properties.sound(SoundAction.get(name), sound));
        return new BotariumFluidType(information, properties);
    }

    /**
     * @deprecated Use {@link #create(FluidInformation)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
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
                return type.information.still();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return type.information.flowing();
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return type.information.overlay();
            }

            @Override
            public @Nullable ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return type.information.screenOverlay();
            }

            @Override
            public int getTintColor() {
                return type.information.tintColor();
            }
        });
    }
}
