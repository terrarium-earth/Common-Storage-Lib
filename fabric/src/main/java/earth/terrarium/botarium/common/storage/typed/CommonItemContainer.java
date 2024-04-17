package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class CommonItemContainer extends CommonWrappedContainer<Item, ItemUnit, ItemVariant, ItemHolder> implements ItemContainer {
    public CommonItemContainer(Storage<ItemVariant> storage) {
        super(storage);
    }

    @Override
    public ItemUnit fromVariant(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    @Override
    public ItemVariant toVariant(ItemUnit unit) {
        return ItemVariant.of(unit.unit(), unit.components());
    }

    @Override
    public ItemHolder createHolder(@Nullable ItemUnit unit, long amount) {
        return unit == null || amount == 0 ? ItemHolder.EMPTY : ItemHolder.of(unit.toStack((int) amount));
    }
}
