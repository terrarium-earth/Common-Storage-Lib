package earth.terrarium.common_storage_lib.context.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.impl.SimpleItemSlot;
import earth.terrarium.common_storage_lib.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.minecraft.world.item.ItemStack;

public record IsolatedSlotContext(SimpleItemSlot mainSlot) implements ItemContext, UpdateManager<ResourceStack<ItemResource>> {
    public IsolatedSlotContext(ItemStack stack) {
        this(new SimpleItemSlot(stack));
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }

    @Override
    public ResourceStack<ItemResource> createSnapshot() {
        return mainSlot.createSnapshot();
    }

    @Override
    public void readSnapshot(ResourceStack<ItemResource> snapshot) {
        mainSlot.readSnapshot(snapshot);
    }

    @Override
    public void update() {
        mainSlot.update();
    }
}
