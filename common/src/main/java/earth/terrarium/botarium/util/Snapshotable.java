package earth.terrarium.botarium.util;

public interface Snapshotable<T> extends Updatable {
    T createSnapshot();
}
