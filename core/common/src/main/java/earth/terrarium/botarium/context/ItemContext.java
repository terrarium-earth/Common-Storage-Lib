package earth.terrarium.botarium.context;

import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageIO;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;

public interface ItemContext extends StorageIO<ItemResource> {
    default <T> T find(ItemLookup<T, ItemContext> lookup) {
        return lookup.find(getUnit().toItemStack((int) getAmount()), this);
    }

    default ItemResource getUnit() {
        return mainSlot().getResource();
    }

    default long getAmount() {
        return mainSlot().getAmount();
    }

    default long insert(ItemResource unit, long amount, boolean simulate) {
        long inserted = mainSlot().insert(unit, amount, simulate);
        long overflow = inserted < amount ? outerContainer().insert(unit, amount - inserted, simulate) : 0;
        if (!simulate) updateAll();
        return inserted + overflow;
    }

    default long extract(ItemResource unit, long amount, boolean simulate) {
        long extract = mainSlot().extract(unit, amount, simulate);
        if (!simulate) updateAll();
        return extract;
    }

    default long exchange(ItemResource newUnit, long amount, boolean simulate) {
        long exchange = TransferUtil.exchange(this, getUnit(), newUnit, amount, simulate);
        if (!simulate) updateAll();
        return exchange;
    }

    default void modify(DataComponentPatch patch) {
        exchange(getUnit().modify(patch), getAmount(), false);
    }

    CommonStorage<ItemResource> outerContainer();

    StorageSlot<ItemResource> mainSlot();

    default void updateAll() {
        UpdateManager.batch(outerContainer(), mainSlot());
    }
}
