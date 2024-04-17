package earth.terrarium.botarium.common.transfer.base;

public interface UnitHolder<T extends TransferUnit<?>> {
    T getUnit();
    long getHeldAmount();
}
