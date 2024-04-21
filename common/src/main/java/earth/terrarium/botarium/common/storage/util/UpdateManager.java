package earth.terrarium.botarium.common.storage.util;

public interface UpdateManager<T> {
    T createSnapshot();

    void readSnapshot(T snapshot);

    void update();

    static void batch(Object... managers) {
        for (Object potential : managers) {
            if (potential instanceof UpdateManager) {
                ((UpdateManager<?>) potential).update();
            }
        }
    }
}
