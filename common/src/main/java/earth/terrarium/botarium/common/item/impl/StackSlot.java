package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;

public class StackSlot implements UnitSlot<ItemUnit> {
    private ItemUnit unit = ItemUnit.BLANK;
    private int amount = 0;
    private int limit = 99;

    public StackSlot(ItemStack stack) {
        if (!stack.isEmpty()) {
            setUnit(ItemUnit.of(stack));
            this.amount = stack.getCount();
        }
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public boolean isValueValid(ItemUnit unit) {
        return true;
    }

    @Override
    public ItemUnit getUnit() {
        return unit;
    }

    public void setUnit(ItemUnit unit) {
        this.unit = unit;
        this.limit = unit.toStack().getMaxStackSize();
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public ItemStack getStack() {
        return unit.toStack(amount);
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        if (this.unit.isBlank()) {
            long inserted = Math.min(amount, limit);
            if (!simulate) {
                setUnit(unit);
                this.amount = (int) inserted;
            }
            return inserted;
        } else if (this.unit.matches(unit)) {
            long inserted = Math.min(amount, limit - this.amount);
            if (!simulate) {
                this.amount += (int) inserted;
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        if (this.unit.matches(unit)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= (int) extracted;
                if (this.amount == 0) {
                    setUnit(ItemUnit.BLANK);
                }
            }
            return extracted;
        }
        return 0;
    }
}
