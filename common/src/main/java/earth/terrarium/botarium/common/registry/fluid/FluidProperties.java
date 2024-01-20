package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public record FluidProperties(ResourceLocation id,
                              double motionScale,
                              boolean canPushEntity,
                              boolean canSwim,
                              boolean canDrown,
                              float fallDistanceModifier,
                              boolean canExtinguish,
                              boolean canConvertToSource,
                              boolean supportsBloating,
                              BlockPathTypes pathType,
                              BlockPathTypes adjacentPathType,
                              boolean canHydrate,
                              int lightLevel,
                              int density,
                              int temperature,
                              int viscosity,
                              Rarity rarity,
                              FluidSounds sounds,
                              ResourceLocation still,
                              ResourceLocation flowing,
                              ResourceLocation overlay,
                              ResourceLocation screenOverlay,
                              int tintColor,
                              int tickDelay,
                              int slopeFindDistance,
                              int dropOff,
                              float explosionResistance,
                              boolean canPlace

) implements FluidInformation {

    @Override
    public FluidProperties toProperties() {
        return this;
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private double motionScale = 0.014;
        private boolean canPushEntity = true;
        private boolean canSwim = false;
        private boolean canDrown = true;
        private float fallDistanceModifier = 0.5f;
        private boolean canExtinguish = false;
        private boolean canConvertToSource = true;
        private boolean supportsBloating = false;
        private BlockPathTypes pathType = BlockPathTypes.WATER;
        private BlockPathTypes adjacentPathType = BlockPathTypes.WATER_BORDER;
        private boolean canHydrate = true;
        private int lightLevel = 0;
        private int density = 1000;
        private int temperature = 300;
        private int viscosity = 1000;
        private Rarity rarity = Rarity.COMMON;
        private final FluidSounds sounds = new FluidSounds();
        private ResourceLocation still;
        private ResourceLocation flowing;
        private ResourceLocation overlay;
        private ResourceLocation screenOverlay;
        private int tintColor = -1;
        private int tickRate = 5;
        private int slopeFindDistance = 4;
        private int dropOff = 1;
        private float explosionResistance = 100.0f;

        private boolean canPlace = true;

        private Builder() {
        }

        public Builder motionScale(double motionScale) {
            this.motionScale = motionScale;
            return this;
        }

        public Builder canPushEntity(boolean canPushEntity) {
            this.canPushEntity = canPushEntity;
            return this;
        }

        public Builder canSwim(boolean canSwim) {
            this.canSwim = canSwim;
            return this;
        }

        public Builder canDrown(boolean canDrown) {
            this.canDrown = canDrown;
            return this;
        }

        public Builder fallDistanceModifier(float fallDistanceModifier) {
            this.fallDistanceModifier = fallDistanceModifier;
            return this;
        }

        public Builder canExtinguish(boolean canExtinguish) {
            this.canExtinguish = canExtinguish;
            return this;
        }

        public Builder canConvertToSource(boolean canConvertToSource) {
            this.canConvertToSource = canConvertToSource;
            return this;
        }

        public Builder supportsBloating(boolean supportsBloating) {
            this.supportsBloating = supportsBloating;
            return this;
        }

        public Builder pathType(BlockPathTypes pathType) {
            this.pathType = pathType;
            return this;
        }

        public Builder adjacentPathType(BlockPathTypes adjacentPathType) {
            this.adjacentPathType = adjacentPathType;
            return this;
        }

        public Builder canHydrate(boolean canHydrate) {
            this.canHydrate = canHydrate;
            return this;
        }

        public Builder lightLevel(int lightLevel) {
            this.lightLevel = lightLevel;
            return this;
        }

        public Builder density(int density) {
            this.density = density;
            return this;
        }

        public Builder temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder viscosity(int viscosity) {
            this.viscosity = viscosity;
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder sounds(String sound, SoundEvent soundEvent) {
            this.sounds.addSound(sound, soundEvent);
            return this;
        }

        public Builder still(ResourceLocation still) {
            this.still = still;
            return this;
        }

        public Builder flowing(ResourceLocation flowing) {
            this.flowing = flowing;
            return this;
        }

        public Builder overlay(ResourceLocation overlay) {
            this.overlay = overlay;
            return this;
        }

        public Builder screenOverlay(ResourceLocation screenOverlay) {
            this.screenOverlay = screenOverlay;
            return this;
        }

        public Builder tintColor(int tintColor) {
            this.tintColor = tintColor;
            return this;
        }

        public Builder tickRate(int tickRate) {
            this.tickRate = tickRate;
            return this;
        }

        public Builder slopeFindDistance(int slopeFindDistance) {
            this.slopeFindDistance = slopeFindDistance;
            return this;
        }

        public Builder dropOff(int dropOff) {
            this.dropOff = dropOff;
            return this;
        }

        public Builder explosionResistance(float explosionResistance) {
            this.explosionResistance = explosionResistance;
            return this;
        }

        public Builder disablePlacing() {
            this.canPlace = false;
            return this;
        }

        public FluidProperties build(ResourceLocation id) {
            return new FluidProperties(id, motionScale, canPushEntity, canSwim, canDrown, fallDistanceModifier, canExtinguish, canConvertToSource, supportsBloating, pathType, adjacentPathType, canHydrate, lightLevel, density, temperature, viscosity, rarity, sounds, still, flowing, overlay, screenOverlay, tintColor, tickRate, slopeFindDistance, dropOff, explosionResistance, canPlace);
        }
    }
}
