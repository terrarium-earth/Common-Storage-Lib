package earth.terrarium.botarium.common.fluid.utils;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record FluidParticleOptions(FluidHolder fluid) implements ParticleOptions {

    @Override
    public @NotNull ParticleType<?> getType() {
        return Botarium.FLUID_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        fluid.writeToBuffer(buffer);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), BuiltInRegistries.FLUID.getKey(fluid.getFluid()));
    }

    public static final Codec<FluidParticleOptions> CODEC = FluidHolder.NEW_CODEC.xmap(FluidParticleOptions::new, FluidParticleOptions::fluid);

    public static final Deserializer<FluidParticleOptions> DESERIALIZER = new Deserializer<FluidParticleOptions>() {

        // TODO Fluid particles on command
        public @NotNull FluidParticleOptions fromCommand(ParticleType<FluidParticleOptions> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new FluidParticleOptions(FluidHolder.of(BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(reader.readQuotedString()))));
        }

        public @NotNull FluidParticleOptions fromNetwork(ParticleType<FluidParticleOptions> particleTypeIn, FriendlyByteBuf buffer) {
            return new FluidParticleOptions(FluidHolder.readFromBuffer(buffer));
        }
    };
}
