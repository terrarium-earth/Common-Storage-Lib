package earth.terrarium.botarium.forge.extensions;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

public class ClientFluidHooksImpl {

    @ImplementsBaseElement
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation resourceLocation = extension.getStillTexture(new ForgeFluidHolder(fluid).getFluidStack());
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
    }

    @ImplementsBaseElement
    public static int getFluidColor(FluidHolder fluid) {
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid.getFluid());
        return extension.getTintColor(new ForgeFluidHolder(fluid).getFluidStack());
    }
}
