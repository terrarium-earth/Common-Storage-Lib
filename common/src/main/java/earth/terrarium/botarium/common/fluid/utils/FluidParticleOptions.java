package earth.terrarium.botarium.common.fluid.utils;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public record FluidParticleOptions(FluidHolder fluid) implements ParticleOptions {

    @Override
    public @NotNull ParticleType<?> getType() {
        return Botarium.FLUID_PARTICLE.get();
    }

    public static final MapCodec<FluidParticleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FluidHolder.CODEC.fieldOf("fluid").forGetter(FluidParticleOptions::fluid)
    ).apply(instance, FluidParticleOptions::new));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FluidParticleOptions> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf object, FluidParticleOptions object2) {

        }

        @Override
        public FluidParticleOptions decode(RegistryFriendlyByteBuf object) {
            return null;
        }
    };
}
