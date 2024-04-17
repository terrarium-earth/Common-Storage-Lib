package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import net.minecraft.util.Tuple;

import java.util.function.Predicate;

public interface SingleSlotContainer<T extends TransferUnit<?>, U extends UnitHolder<T>> extends UpdateManager, UnitHolder<T> {
    long getLimit();

    boolean isValueValid(T value);

    long insert(T value, long amount, boolean simulate);

    U extract(Predicate<T> predicate, long amount, boolean simulate);
}
