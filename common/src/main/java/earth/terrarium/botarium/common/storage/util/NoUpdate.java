package earth.terrarium.botarium.common.storage.util;

public interface NoUpdate extends UpdateManager<Byte> {
    @Override
    default Byte createSnapshot() {
        return 0;
    }

    @Override
    default void readSnapshot(Byte snapshot) {}

    @Override
    default void update() {}
}
