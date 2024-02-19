package earth.terrarium.botarium.common.item;

@Deprecated
public interface ItemContainerBlock {

    /**
     * @return A {@link SerializableContainer} that represents the inventory of this block.
     */
    SerializableContainer getContainer();
}
