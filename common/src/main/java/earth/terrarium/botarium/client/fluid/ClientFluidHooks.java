package earth.terrarium.botarium.client.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.NotImplementedException;

public class ClientFluidHooks {
    public static TextureAtlasSprite getFluidSprite(FluidStack fluid) {
        throw new NotImplementedException();
    }

    public static int getFluidColor(FluidStack fluid) {
        throw new NotImplementedException();
    }

    public static int getFluidLightLevel(FluidStack fluid) {
        throw new NotImplementedException();
    }

    public static Component getDisplayName(FluidStack fluid) {
        throw new NotImplementedException();
    }
}
