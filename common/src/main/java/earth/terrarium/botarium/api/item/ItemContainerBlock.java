package earth.terrarium.botarium.api.item;

public interface ItemContainerBlock {

    /**
     * @return A {@link SerializbleContainer} that represents the inventory of this block.
     */
    SerializbleContainer getContainer();
}
