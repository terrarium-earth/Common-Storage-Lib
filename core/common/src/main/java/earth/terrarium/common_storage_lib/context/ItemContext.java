package earth.terrarium.common_storage_lib.context;

import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageIO;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.util.TransferUtil;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;

public interface ItemContext extends StorageIO<ItemResource>, DataComponentHolder {
    default <T> T find(ItemLookup<T, ItemContext> lookup) {
        return lookup.find(getResource().toStack((int) getAmount()), this);
    }

    default boolean isPresent(ItemLookup<?, ItemContext> lookup) {
        return lookup.isPresent(getResource().getCachedStack(), this);
    }

    default ItemResource getResource() {
        return mainSlot().getResource();
    }

    default long getAmount() {
        return mainSlot().getAmount();
    }

    default long insert(ItemResource resource, long amount, boolean simulate) {
        long inserted = mainSlot().insert(resource, amount, simulate);
        long overflow = inserted < amount ? outerContainer().insert(resource, amount - inserted, simulate) : 0;
        if (!simulate) updateAll();
        return inserted + overflow;
    }

    default long extract(ItemResource resource, long amount, boolean simulate) {
        long extract = mainSlot().extract(resource, amount, simulate);
        if (!simulate) updateAll();
        return extract;
    }

    default long exchange(ItemResource newResource, long amount, boolean simulate) {
        long exchange = TransferUtil.exchange(this, getResource(), newResource, amount, simulate);
        if (!simulate) updateAll();
        return exchange;
    }

    /**
     * Applies a data component patch to the item in the main context.
     * @param patch The patch to apply
     * @return Whether the patch was successfully applied
     */
    default boolean modify(DataComponentPatch patch) {
        return exchange(getResource().modify(patch), getAmount(), false) == getAmount();
    }

    default <T> boolean set(DataComponentType<T> type, T value) {
        return modify(DataComponentPatch.builder().set(type, value).build());
    }

    @Override
    default @NotNull DataComponentMap getComponents() {
        return getResource().getComponents();
    }

    CommonStorage<ItemResource> outerContainer();

    StorageSlot<ItemResource> mainSlot();

    default void updateAll() {
        UpdateManager.batch(outerContainer(), mainSlot());
    }
}
