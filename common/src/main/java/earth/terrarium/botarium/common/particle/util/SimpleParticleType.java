package earth.terrarium.botarium.common.particle.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class SimpleParticleType<T extends ParticleOptions> extends ParticleType<T> {
    private final MapCodec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;

    public SimpleParticleType(boolean bl, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        super(bl);
        this.codec = codec;
        this.streamCodec = streamCodec;
    }

    @Override
    public @NotNull MapCodec<T> codec() {
        return codec;
    }

    @Override
    public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }
}
