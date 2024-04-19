package earth.terrarium.botarium.common.context;

import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitIO;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

public interface ItemContext extends UnitIO<ItemUnit> {
    default <T> T find(ItemLookup<T, ItemContext> lookup) {
        return lookup.find(getUnit().toStack((int) getAmount()), this);
    }

    default ItemUnit getUnit() {
        return mainSlot().getUnit();
    }

    default long getAmount() {
        return mainSlot().getAmount();
    }

    default long insert(ItemUnit unit, long amount, boolean simulate) {
        long inserted = mainSlot().insert(unit, amount, simulate);
        long overflow = outerContainer().insert(unit, amount - inserted, simulate);
        return inserted + overflow;
    }

    default long extract(ItemUnit unit, long amount, boolean simulate) {
        return mainSlot().extract(unit, amount, simulate);
    }

    default long exchange(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.exchange(mainSlot(), unit, amount, simulate);
    }

    default boolean modify(DataComponentPatch patch, boolean simulate) {
        ItemStack modifyStack = mainSlot().getUnit().toStack();
        modifyStack.applyComponents(patch);
        long amount = mainSlot().getAmount();
        return amount == exchange(ItemUnit.of(modifyStack), amount, simulate);
    }

    UnitContainer<ItemUnit> outerContainer();

    UnitSlot<ItemUnit> mainSlot();
}
