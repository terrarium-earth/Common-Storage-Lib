package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(ClientFluidHooks.class)
public class ClientFluidHooksImpl {

    @ImplementsBaseElement
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        FluidAttributes fluidAttributes = fluid.getFluid().getAttributes();
        ResourceLocation resourceLocation = fluidAttributes.getStillTexture(new ForgeFluidHolder(fluid).getFluidStack());
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
    }

    @ImplementsBaseElement
    public static int getFluidColor(FluidHolder fluid) {
        FluidAttributes fluidAttributes = fluid.getFluid().getAttributes();
        return fluidAttributes.getColor(new ForgeFluidHolder(fluid).getFluidStack());
    }
}
