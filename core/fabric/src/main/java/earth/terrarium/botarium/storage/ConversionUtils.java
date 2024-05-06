package earth.terrarium.botarium.storage;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.item.ItemResource;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

public class ConversionUtils {
    public static ItemResource toResource(ItemVariant variant) {
        return ItemResource.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemResource resource) {
        return ItemVariant.of(resource.getType(), resource.getDataPatch());
    }

    public static FluidResource toResource(FluidVariant variant) {
        return FluidResource.of(variant.getFluid(), variant.getComponents());
    }

    public static FluidVariant toVariant(FluidResource resource) {
        return FluidVariant.of(resource.getType(), resource.getDataPatch());
    }
}
