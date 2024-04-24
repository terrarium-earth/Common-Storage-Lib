package earth.terrarium.botarium.common.storage;

import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;

public class ConversionUtils {
    public static ItemUnit toUnit(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemUnit unit) {
        return ItemVariant.of(unit.type(), unit.components());
    }

    public static FluidUnit toUnit(FluidVariant variant) {
        return FluidUnit.of(variant.getFluid(), variant.getComponents());
    }

    public static FluidVariant toVariant(FluidUnit unit) {
        return FluidVariant.of(unit.type(), unit.components());
    }
}
