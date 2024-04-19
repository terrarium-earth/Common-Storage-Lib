package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.transfer.base.TransferUnit;

public interface UnitIO<T extends TransferUnit<?>> {
    long insert(T unit, long amount, boolean simulate);

    long extract(T unit, long amount, boolean simulate);
}
