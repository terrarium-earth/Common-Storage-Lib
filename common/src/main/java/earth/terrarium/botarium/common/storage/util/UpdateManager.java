package earth.terrarium.botarium.common.storage.util;

public interface UpdateManager<T> {
    T createSnapshot();

    void readSnapshot(T snapshot);

    void update();
}
