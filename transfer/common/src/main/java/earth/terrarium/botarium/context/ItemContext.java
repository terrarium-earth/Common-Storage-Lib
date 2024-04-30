package earth.terrarium.botarium.context;

import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageIO;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

public interface ItemContext extends StorageIO<ItemUnit> {
    default <T> T find(ItemLookup<T, ItemContext> lookup) {
        return lookup.find(getUnit().toItemStack((int) getAmount()), this);
    }

    default ItemUnit getUnit() {
        return mainSlot().getUnit();
    }

    default long getAmount() {
        return mainSlot().getAmount();
    }

    default long insert(ItemUnit unit, long amount, boolean simulate) {
        long inserted = mainSlot().insert(unit, amount, simulate);
        long overflow = inserted < amount ? outerContainer().insert(unit, amount - inserted, simulate) : 0;
        if (!simulate) updateAll();
        return inserted + overflow;
    }

    default long extract(ItemUnit unit, long amount, boolean simulate) {
        long extract = mainSlot().extract(unit, amount, simulate);
        if (!simulate) updateAll();
        return extract;
    }

    default long exchange(ItemUnit newUnit, long amount, boolean simulate) {
        long exchange = TransferUtil.exchange(this, getUnit(), newUnit, amount, simulate);
        if (!simulate) updateAll();
        return exchange;
    }

    default void modify(DataComponentPatch patch) {
        exchange(getUnit().modify(patch), getAmount(), false);
    }

    CommonStorage<ItemUnit> outerContainer();

    StorageSlot<ItemUnit> mainSlot();

    default void updateAll() {
        UpdateManager.batch(outerContainer(), mainSlot());
    }
}
