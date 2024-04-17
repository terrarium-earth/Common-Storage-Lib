package earth.terrarium.botarium.common.particle;

import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

@Expect
public class BotariumParticles {

    public static final Supplier<ParticleType<FluidParticleOptions>> FLUID_PARTICLE;
}
