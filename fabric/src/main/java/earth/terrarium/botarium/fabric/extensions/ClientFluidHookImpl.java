package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.ClientFluidHooks;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

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

    @ImplementsBaseElement
    public static int getFluidLightLevel(FluidHolder fluid) {
        return FluidVariantAttributes.getLuminance(FabricFluidHolder.of(fluid).toVariant());
    }

    @ImplementedByExtension
    public static Component getDisplayName(FluidHolder fluid) {
        return FluidVariantAttributes.getName(FabricFluidHolder.of(fluid).toVariant());
    }

}
