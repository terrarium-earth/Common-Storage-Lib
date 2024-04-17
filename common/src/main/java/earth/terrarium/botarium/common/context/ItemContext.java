package earth.terrarium.botarium.common.context;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface ItemContext {
    Predicate<ItemUnit> ANY = (ignored) -> true;

    default long insert(ItemUnit unit, long amount, boolean simulate) {
        long inserted = mainSlot().insert(unit, amount, simulate);
        long overflow = insertIndiscriminately(unit, amount - inserted, simulate);
        return inserted + overflow;
    }

    default ItemHolder extract(Predicate<ItemUnit> predicate, long amount, boolean simulate) {
        return mainSlot().extract(predicate, amount, simulate);
    }

    default long exchange(ItemUnit unit, long amount, boolean simulate) {
        ItemHolder tempExtract = extract(ANY, amount, true);
        if (tempExtract.getHeldAmount() == 0) {
            return 0;
        } else {
            ItemHolder extracted = extract(ANY, amount, false);
            long inserted = insert(unit, amount, true);
            if (inserted == extracted.getHeldAmount()) {
                insert(unit, amount, simulate);
                return inserted;
            } else {
                insert(extracted.getUnit(), extracted.getHeldAmount(), false);
            }
        }
        return 0;
    }

    default long insertIndiscriminately(ItemUnit unit, long amount, boolean simulate) {
        return outerContainer().insert(unit, amount, simulate);
    }

    ItemContainer outerContainer();

    SingleSlotContainer<ItemUnit, ItemHolder> mainSlot();
}
