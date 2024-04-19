package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;

public record ModifyOnlyContext(ItemStack stack) implements ItemContext {
    @Override
    public long exchange(ItemUnit unit, long amount, boolean simulate) {
        if (!unit.isOf(stack.getItem()) || amount != stack.getCount()) return 0;
        if (!simulate) {
            if (stack.getComponents() instanceof PatchedDataComponentMap map) {
                map.restorePatch(unit.components());
            } else {
                stack.applyComponents(unit.components());
            }
        }
        return amount;
    }

    @Override
    public boolean modify(DataComponentPatch patch, boolean simulate) {
        if (!simulate) {
            stack.applyComponents(patch);
        }
        return true;
    }

    @Override
    public UnitContainer<ItemUnit> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }

    @Override
    public UnitSlot<ItemUnit> mainSlot() {
        return new ModifyOnlyContainer(stack);
    }

    public record ModifyOnlyContainer(ItemStack stack) implements UnitSlot<ItemUnit> {
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
