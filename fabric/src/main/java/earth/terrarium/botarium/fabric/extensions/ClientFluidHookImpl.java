package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.fluid.ClientFluidHooks;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.fabric.fluid.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(ClientFluidHooks.class)
public class ClientFluidHookImpl {

    @ImplementsBaseElement
    public static TextureAtlasSprite getFluidSprite(FluidHolder fluid) {
        return FluidVariantRendering.getSprite(FabricFluidHolder.of(fluid).toVariant());
    }

    @ImplementsBaseElement
    public static int getFluidColor(FluidHolder fluid) {
        return FluidVariantRendering.getColor(FabricFluidHolder.of(fluid).toVariant());
    }

}
