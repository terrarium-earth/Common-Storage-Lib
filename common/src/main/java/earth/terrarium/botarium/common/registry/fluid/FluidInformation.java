package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public interface FluidInformation {

    ResourceLocation id();

    double motionScale();

    boolean canPushEntity() ;

    boolean canSwim();

    boolean canDrown();

    float fallDistanceModifier();

    boolean canExtinguish();

    boolean canConvertToSource();

    boolean supportsBloating();

    BlockPathTypes pathType();

    BlockPathTypes adjacentPathType();

    boolean canHydrate();

    int lightLevel();

    int density();

    int temperature();

    int viscosity();

    Rarity rarity();

    FluidSounds sounds();

    ResourceLocation still();

    ResourceLocation flowing();

    ResourceLocation overlay();

    ResourceLocation screenOverlay();

    int tintColor();

    int tickDelay();

    int slopeFindDistance();

    int dropOff();

    float explosionResistance();

    boolean canPlace();

    FluidProperties toProperties();
}
