package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(ClientFluidHooks.class)
public class ClientFluidHooksImpl {

    @ImplementsBaseElement
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid.getFluid());
        ResourceLocation resourceLocation = extension.getStillTexture(ForgeFluidHolder.toStack(fluid));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
    }

    @ImplementsBaseElement
    public static int getFluidColor(FluidHolder fluid) {
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(fluid.getFluid());
        return extension.getTintColor(ForgeFluidHolder.toStack(fluid));
    }

    @ImplementsBaseElement
    public static int getFluidLightLevel(FluidHolder fluid) {
        return fluid.getFluid().getFluidType().getLightLevel();
    }

    @ImplementedByExtension
    public static Component getDisplayName(FluidHolder fluid) {
        return ForgeFluidHolder.toStack(fluid).getDisplayName();
    }
}
