package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.item.Item;

public class CommonItemContainer extends CommonWrappedContainer<Item, ItemUnit, ItemVariant> implements ItemContainer {
    public CommonItemContainer(Storage<ItemVariant> storage) {
        super(storage);
    }

    @Override
    public ItemUnit toUnit(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    @Override
    public ItemVariant toVariant(ItemUnit unit) {
        return ItemVariant.of(unit.unit(), unit.components());
    }
}
