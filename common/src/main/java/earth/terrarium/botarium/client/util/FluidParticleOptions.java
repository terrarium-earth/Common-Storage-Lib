package earth.terrarium.botarium.client.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FluidParticleOptions implements ParticleOptions {

	private final FluidHolder fluid;

	public FluidParticleOptions(FluidHolder fluid) {
		this.fluid = fluid;
	}

	public ParticleProvider<FluidParticleOptions> getFactory() {
		return (data, world, x, y, z, vx, vy, vz) -> new FluidHolderParticle(world, data.fluid, x, y, z, vx, vy, vz);
	}

	@Override
	public @NotNull ParticleType<?> getType() {
		return Botarium.FLUID_PARTICLE.get();
	}

	public FluidHolder getFluid() {
		return fluid;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		fluid.writeToBuffer(buffer);
	}

	@Override
	public @NotNull String writeToString() {
		return String.format("%s %s", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), BuiltInRegistries.FLUID.getKey(fluid.getFluid()));
	}

	public static final Codec<FluidParticleOptions> CODEC = FluidHolder.CODEC.xmap(FluidParticleOptions::new, FluidParticleOptions::getFluid);

	public static final ParticleOptions.Deserializer<FluidParticleOptions> DESERIALIZER =
		new ParticleOptions.Deserializer<FluidParticleOptions>() {

			// TODO Fluid particles on command
			public @NotNull FluidParticleOptions fromCommand(ParticleType<FluidParticleOptions> particleTypeIn, StringReader reader)
				throws CommandSyntaxException {
				reader.expect(' ');
				return new FluidParticleOptions(FluidHolder.of(BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(reader.readQuotedString()))));
			}

			public @NotNull FluidParticleOptions fromNetwork(ParticleType<FluidParticleOptions> particleTypeIn, FriendlyByteBuf buffer) {
				return new FluidParticleOptions(FluidHolder.readFromBuffer(buffer));
			}
		};
}
