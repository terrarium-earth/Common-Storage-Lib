package earth.terrarium.common_storage_lib.context.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.impl.SimpleItemSlot;
import earth.terrarium.common_storage_lib.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.world.item.ItemStack;

public record IsolatedSlotContext(SimpleItemSlot mainSlot) implements ItemContext {
    public IsolatedSlotContext(ItemStack stack) {
        this(new SimpleItemSlot(stack));
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }
}
