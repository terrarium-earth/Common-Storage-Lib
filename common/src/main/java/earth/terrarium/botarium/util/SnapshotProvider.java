package earth.terrarium.botarium.util;

public interface SnapshotProvider<T> {
    T createSnapshot();
    void loadSnapshot(T snapshot);
}
