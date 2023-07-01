package earth.terrarium.botarium.common.fluid.utils;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class ClientFluidHooks {
    @ImplementedByExtension
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static int getFluidColor(FluidHolder fluid) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static Component getDisplayName(FluidHolder fluid) {
        throw new NotImplementedException();
    }
}
