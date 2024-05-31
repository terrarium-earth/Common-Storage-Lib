package earth.terrarium.common_storage_lib.context.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;

public record ModifyOnlyContext(ItemStack stack) implements ItemContext {
    @Override
    public long exchange(ItemResource newResource, long amount, boolean simulate) {
        if (!newResource.isOf(stack.getItem()) || amount != stack.getCount()) return 0;
        if (!simulate) {
            if (stack.getComponents() instanceof PatchedDataComponentMap map) {
                map.restorePatch(newResource.getDataPatch());
            } else {
                stack.applyComponents(newResource.getDataPatch());
            }
        }
        return amount;
    }

    @Override
    public void modify(DataComponentPatch patch) {
        stack.applyComponents(patch);
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }

    @Override
    public StorageSlot<ItemResource> mainSlot() {
        return new ModifyOnlyContainer(stack);
    }

    public record ModifyOnlyContainer(ItemStack stack) implements StorageSlot<ItemResource> {
        @Override
        public long getLimit(ItemResource resource) {
            return resource.getCachedStack().getMaxStackSize();
        }

        @Override
        public boolean isResourceValid(ItemResource resource) {
            return resource.test(stack);
        }

        @Override
        public ItemResource getResource() {
            return ItemResource.of(stack);
        }

        @Override
        public long getAmount() {
            return stack.getCount();
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            if (resource.test(stack)) {
                long inserted = Math.min(amount, getLimit(resource) - stack.getCount());
                if (!simulate) {
                    stack.grow((int) inserted);
                }
                return inserted;
            }
            return 0;
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            if (resource.test(stack)) {
                long extracted = Math.min(amount, stack.getCount());
                if (!simulate) {
                    stack.shrink((int) extracted);
                }
                return extracted;
            }
            return 0;
        }
    }
}
