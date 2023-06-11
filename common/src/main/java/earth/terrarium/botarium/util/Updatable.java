package earth.terrarium.botarium.util;

public interface Updatable<T> {

    /**
     * Called when the operation has been completed and the data has been updated.
     */
    void update(T object);
}
