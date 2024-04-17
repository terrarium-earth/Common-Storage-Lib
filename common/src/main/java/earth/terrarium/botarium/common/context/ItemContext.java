package earth.terrarium.botarium.common.context;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;

import java.util.function.Predicate;

public interface ItemContext {
    Predicate<ItemUnit> ANY = (ignored) -> true;

    default long insert(ItemUnit unit, long amount, boolean simulate) {
        long inserted = mainSlot().insert(unit, amount, simulate);
        long overflow = insertIndiscriminately(unit, amount - inserted, simulate);
        return inserted + overflow;
    }

    default long extract(ItemUnit unit, long amount, boolean simulate) {
        return mainSlot().extract(unit, amount, simulate);
    }

    default long exchange(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.exchange(mainSlot(), unit, amount, simulate);
    }

    default long insertIndiscriminately(ItemUnit unit, long amount, boolean simulate) {
        return outerContainer().insert(unit, amount, simulate);
    }

    ItemContainer outerContainer();

    ContainerSlot<ItemUnit> mainSlot();
}
