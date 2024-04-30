package earth.terrarium.botarium.context.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;

public record ModifyOnlyContext(ItemStack stack) implements ItemContext {
    @Override
    public long exchange(ItemUnit newUnit, long amount, boolean simulate) {
        if (!newUnit.isOf(stack.getItem()) || amount != stack.getCount()) return 0;
        if (!simulate) {
            if (stack.getComponents() instanceof PatchedDataComponentMap map) {
                map.restorePatch(newUnit.getDataPatch());
            } else {
                stack.applyComponents(newUnit.getDataPatch());
            }
        }
        return amount;
    }

    @Override
    public void modify(DataComponentPatch patch) {
        stack.applyComponents(patch);
    }

    @Override
    public CommonStorage<ItemUnit> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }

    @Override
    public StorageSlot<ItemUnit> mainSlot() {
        return new ModifyOnlyContainer(stack);
    }

    public record ModifyOnlyContainer(ItemStack stack) implements StorageSlot<ItemUnit> {
        @Override
        public long getLimit() {
            return stack.getMaxStackSize();
        }

        @Override
        public boolean isValueValid(ItemUnit unit) {
            return unit.matches(stack);
        }

        @Override
        public ItemUnit getUnit() {
            return ItemUnit.of(stack);
        }

        @Override
        public long getAmount() {
            return stack.getCount();
        }

        @Override
        public boolean isBlank() {
            return stack.isEmpty();
        }

        @Override
        public long insert(ItemUnit unit, long amount, boolean simulate) {
            if (unit.matches(stack)) {
                long inserted = Math.min(amount, getLimit() - stack.getCount());
                if (!simulate) {
                    stack.grow((int) inserted);
                }
                return inserted;
            }
            return 0;
        }

        @Override
        public long extract(ItemUnit unit, long amount, boolean simulate) {
            if (unit.matches(stack)) {
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
