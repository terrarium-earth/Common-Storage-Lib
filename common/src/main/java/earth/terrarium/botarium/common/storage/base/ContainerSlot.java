package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;

public interface ContainerSlot<T extends TransferUnit<?>> extends UpdateManager {
    long getLimit();

    boolean isValueValid(T unit);

    T getUnit();

    long getAmount();

    long insert(T unit, long amount, boolean simulate);

    long extract(T unit, long amount, boolean simulate);
}
