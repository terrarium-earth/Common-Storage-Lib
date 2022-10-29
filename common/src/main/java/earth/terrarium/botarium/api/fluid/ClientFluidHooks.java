package earth.terrarium.botarium.api.fluid;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
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
}
