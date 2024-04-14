package earth.terrarium.botarium.client.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.NotImplementedException;

public class ClientFluidHooks {
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    public static int getFluidColor(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    public static int getFluidLightLevel(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    public static Component getDisplayName(FluidHolder fluid) {
        throw new NotImplementedException();
    }
}
