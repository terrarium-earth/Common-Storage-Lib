package earth.terrarium.botarium.storage;

import earth.terrarium.botarium.resource.fluid.FluidResource;
import earth.terrarium.botarium.resource.item.ItemResource;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

public class ConversionUtils {
    public static ItemResource toUnit(ItemVariant variant) {
        return ItemResource.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemResource unit) {
        return ItemVariant.of(unit.getType(), unit.getDataPatch());
    }

    public static FluidResource toUnit(FluidVariant variant) {
        return FluidResource.of(variant.getFluid(), variant.getComponents());
    }

    public static FluidVariant toVariant(FluidResource unit) {
        return FluidVariant.of(unit.getType(), unit.getDataPatch());
    }
}
