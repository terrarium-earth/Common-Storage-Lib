package earth.terrarium.botarium.common.item;

public interface ItemSnapshot {
    /**
     * Loads the snapshot into the given container
     *
     * @param container The container to load the snapshot into
     */
    void loadSnapshot(ItemContainer container);
}
