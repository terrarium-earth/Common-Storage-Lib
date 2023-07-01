package testmod;

import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import earth.terrarium.botarium.common.registry.fluid.FluidSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class TestFluidInformation implements FluidInformation {

    private static final FluidInformation DEFAULT = FluidProperties.create()
            .still(new ResourceLocation("minecraft:block/water_still"))
            .flowing(new ResourceLocation("minecraft:block/water_flow"))
            .overlay(new ResourceLocation("minecraft:block/water_overlay"))
            .screenOverlay(new ResourceLocation("textures/misc/underwater.png"))
            .tintColor(0x00FF00)
            .sounds("bucket_empty", SoundEvents.AXE_STRIP)
            .sounds("bucket_fill", SoundEvents.GLASS_BREAK)
            .build(null);


    @Override
    public ResourceLocation id() {
        return new ResourceLocation("testmod", "test");
    }

    @Override
    public double motionScale() {
        return DEFAULT.motionScale();
    }

    @Override
    public boolean canPushEntity() {
        return DEFAULT.canPushEntity();
    }

    @Override
    public boolean canSwim() {
        return DEFAULT.canSwim();
    }

    @Override
    public boolean canDrown() {
        return DEFAULT.canDrown();
    }

    @Override
    public float fallDistanceModifier() {
        return DEFAULT.fallDistanceModifier();
    }

    @Override
    public boolean canExtinguish() {
        return DEFAULT.canExtinguish();
    }

    @Override
    public boolean canConvertToSource() {
        return DEFAULT.canConvertToSource();
    }

    @Override
    public boolean supportsBloating() {
        return DEFAULT.supportsBloating();
    }

    @Override
    public BlockPathTypes pathType() {
        return DEFAULT.pathType();
    }

    @Override
    public BlockPathTypes adjacentPathType() {
        return DEFAULT.adjacentPathType();
    }

    @Override
    public boolean canHydrate() {
        return DEFAULT.canHydrate();
    }

    @Override
    public int lightLevel() {
        return DEFAULT.lightLevel();
    }

    @Override
    public int density() {
        return DEFAULT.density();
    }

    @Override
    public int temperature() {
        return DEFAULT.temperature();
    }

    @Override
    public int viscosity() {
        return DEFAULT.viscosity();
    }

    @Override
    public Rarity rarity() {
        return DEFAULT.rarity();
    }

    @Override
    public FluidSounds sounds() {
        return DEFAULT.sounds();
    }

    @Override
    public ResourceLocation still() {
        return DEFAULT.still();
    }

    @Override
    public ResourceLocation flowing() {
        return DEFAULT.flowing();
    }

    @Override
    public ResourceLocation overlay() {
        return DEFAULT.overlay();
    }

    @Override
    public ResourceLocation screenOverlay() {
        return DEFAULT.screenOverlay();
    }

    @Override
    public int tintColor() {
        // This requires a block update to see the color change
        return System.currentTimeMillis() / 1000 % 10 <= 5 ? 0x00FF00 : 0xFF0000;
    }

    @Override
    public int tickDelay() {
        return DEFAULT.tickDelay();
    }

    @Override
    public int slopeFindDistance() {
        return DEFAULT.slopeFindDistance();
    }

    @Override
    public int dropOff() {
        return DEFAULT.dropOff();
    }

    @Override
    public float explosionResistance() {
        return DEFAULT.explosionResistance();
    }

    @Override
    public boolean canPlace() {
        return DEFAULT.canPlace();
    }

    @Override
    public FluidProperties toProperties() {
        return new FluidProperties(
                id(),
                motionScale(),
                canPushEntity(),
                canSwim(),
                canDrown(),
                fallDistanceModifier(),
                canExtinguish(),
                canConvertToSource(),
                supportsBloating(),
                pathType(),
                adjacentPathType(),
                canHydrate(),
                lightLevel(),
                density(),
                temperature(),
                viscosity(),
                rarity(),
                sounds(),
                still(),
                flowing(),
                overlay(),
                screenOverlay(),
                tintColor(),
                tickDelay(),
                slopeFindDistance(),
                dropOff(),
                explosionResistance(),
                canPlace()
        );
    }
}
