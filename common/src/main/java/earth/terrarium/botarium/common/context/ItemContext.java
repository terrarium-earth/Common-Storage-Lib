package earth.terrarium.botarium.common.context;

import earth.terrarium.botarium.common.storage.base.BasicContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemContext extends BasicContainer<ItemStack> {
    default long insert(@NotNull ItemStack value, boolean simulate) {
        long inserted = mainSlot().insert(value, simulate);
        ItemStack remaining = value.copy();
        remaining.shrink((int) inserted);
        long overflow = insertIndiscriminately(remaining, simulate);
        return inserted + overflow;
    }

    default ItemStack extract(long amount, boolean simulate) {
        return mainSlot().extract(amount, simulate);
    }

    default long exchange(@NotNull ItemStack value, boolean simulate) {
        ItemStack tempExtract = extract(value.getCount(), true);
        if (tempExtract.isEmpty()) {
            return 0;
        } else {
            ItemStack extracted = extract(value.getCount(), false);
            long inserted = insert(value.copyWithCount(extracted.getCount()), true);
            if (inserted == extracted.getCount()) {
                insert(value.copyWithCount(extracted.getCount()), simulate);
                return inserted;
            } else {
                insert(extracted, false);
            }
        }
        return 0;
    }

    default long insertIndiscriminately(@NotNull ItemStack value, boolean simulate) {
        return outerContainer().insert(value, simulate);
    }

    SlottedContainer<ItemStack> outerContainer();

    SingleSlotContainer<ItemStack> mainSlot();
}
