package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Serializable;
import net.minecraft.core.Direction;

public interface EnergyContainer extends Serializable {

    /**
     * Inserts a given amount of energy into the container.
     * @param maxAmount The amount to be inserted into the container.
     * @param simulate If true, the container will not be modified.
     * @return The amount of energy that was added to the container.
     */
    long insertEnergy(long maxAmount, boolean simulate);

    /**
     * Extracts a given amount of energy into the container.
     * @param maxAmount The amount to be extracted from the container.
     * @param simulate If true, the container will not be modified.
     * @return The amount of energy that was removed from the container.
     */
    long extractEnergy(long maxAmount, boolean simulate);

    /**
     * Sets a given amount of energy in the container.
     * @param energy The amount of energy to set in the container.
     */
    void setEnergy(long energy);

    /**
     * @return The amount of energy in the container.
     */
    long getStoredEnergy();

    /**
     * @return The maximum amount of energy that can be stored in the container.
     */
    long getMaxCapacity();

    /**
     * @return The maximum amount of energy that can be inserted into the container at a time.
     */
    long maxInsert();

    /**
     * @return The maximum amount of energy that can be extracted from the container at a time.
     */
    long maxExtract();

    /**
     * @return Weather the container allows for energy to be inserted.
     */
    boolean allowsInsertion();

    /**
     * @return Weather the container allows for energy to be extracted.
     */
    boolean allowsExtraction();

    /**
     * @return A {@link EnergySnapshot} of the given state of the {@link EnergyContainer}.
     */
    EnergySnapshot createSnapshot();

    /**
     * Sets the {@link EnergyContainer} with the given state of {@link EnergySnapshot}.
     * @param snapshot The {@link EnergySnapshot} to set the {@link EnergyContainer} to.
     */
    default void readSnapshot(EnergySnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }

    /**
     * @param direction The direction to check.
     * @return A {@link EnergyContainer} from the given {@link Direction}.
     */
    default EnergyContainer getContainer(Direction direction) {
        return this;
    }
}
