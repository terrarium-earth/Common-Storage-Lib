package earth.terrarium.botarium.common.particle;

import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public class BotariumParticles {
    @Expect
    public static final Supplier<ParticleType<FluidParticleOptions>> FLUID_PARTICLE;
}
