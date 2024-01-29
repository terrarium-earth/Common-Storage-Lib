package earth.terrarium.botarium;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import earth.terrarium.botarium.common.registry.RegistryHolder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Botarium {
    public static final String MOD_ID = "botarium";
    public static final String BOTARIUM_DATA = "BotariumData";

    public static final RegistryHolder<ParticleType<?>> PARTICLES = new RegistryHolder<>(BuiltInRegistries.PARTICLE_TYPE, MOD_ID);

    public static final Supplier<ParticleType<FluidParticleOptions>> FLUID_PARTICLE = PARTICLES.register("fluid", () -> new ParticleType<>(false, FluidParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<FluidParticleOptions> codec() {
            return FluidParticleOptions.CODEC;
        }
    });

    public static void init() {
        PARTICLES.initialize();
    }
}