package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.world.item.Item;

public class FabricItemContainer<S> extends FabricWrappedContainer<Item, ItemUnit, ItemVariant, ItemHolder, S, ItemContainer<S>>{
    public FabricItemContainer(ItemContainer<S> container) {
        super(container);
    }

    @Override
    public ItemUnit fromVariant(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    @Override
    public ItemVariant toVariant(ItemUnit unit) {
        return ItemVariant.of(unit.unit(), unit.components());
    }
}
