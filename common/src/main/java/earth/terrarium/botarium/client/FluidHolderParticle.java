package earth.terrarium.botarium.client;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class FluidHolderParticle extends TextureSheetParticle {
	private final FluidHolder fluid;
	public final float u0, u1, v0, v1;

	public FluidHolderParticle(ClientLevel world, FluidHolder fluid, double x, double y, double z, double vx, double vy,
							   double vz) {
		super(world, x, y, z, vx, vy, vz);

		this.fluid = fluid;
		this.setSprite(ClientFluidHooks.getFluidSprite(fluid));

		this.gravity = 1.0F;
		this.rCol = 0.8F;
		this.gCol = 0.8F;
		this.bCol = 0.8F;
		this.multiplyColor(ClientFluidHooks.getFluidColor(fluid));

		this.xd = vx;
		this.yd = vy;
		this.zd = vz;

		this.quadSize /= 2.0F;
		var width = (sprite.getU1() - sprite.getU0()) / 3.0F;
		var height = (sprite.getV1() - sprite.getV0()) / 3.0F;
		float xOffset = (float) (width * Math.random());
		float yOffset = (float) (height * Math.random());
		u0 = sprite.getU0() + xOffset;
		u1 = sprite.getU0() + xOffset + width;
		v0 = sprite.getV0() + yOffset;
		v1 = sprite.getV0() + yOffset + height;
	}

	@Override
	protected int getLightColor(float p_189214_1_) {
		int brightnessForRender = super.getLightColor(p_189214_1_);
		int skyLight = brightnessForRender >> 20;
		int blockLight = (brightnessForRender >> 4) & 0xf;
		blockLight = Math.max(blockLight, ClientFluidHooks.getFluidLightLevel(fluid));
		return (skyLight << 20) | (blockLight << 4);
	}

	protected void multiplyColor(int color) {
		this.rCol *= (float) (color >> 16 & 255) / 255.0F;
		this.gCol *= (float) (color >> 8 & 255) / 255.0F;
		this.bCol *= (float) (color & 255) / 255.0F;
	}

	@Override
	protected float getU0() {
		return u0;
	}

	@Override
	protected float getU1() {
		return u1;
	}

	@Override
	protected float getV0() {
		return v0;
	}

	@Override
	protected float getV1() {
		return v1;
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.TERRAIN_SHEET;
	}
}