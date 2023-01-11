package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.util.Serializable;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;

public interface EnergyContainer extends Serializable, Clearable {

    /**
     * @param direction The {@link Direction} to get the container from.
     * @return A {@link EnergyContainer} for a given {@link Direction}.
     */
    default EnergyContainer getContainer(Direction direction) {
        return this;
    }

    /**
     * Inserts a given amount of energy into the container.
     * @param maxAmount The amount to be inserted into the container.
     * @param simulate If true, the container will not be modified.
     * @return The amount of energy that was added to the container.
     */
    long insertEnergy(long maxAmount, boolean simulate);

    default long internalInsert(long amount, boolean simulate) {
        return insertEnergy(amount, simulate);
    }

    /**
     * Extracts a given amount of energy into the container.
     * @param maxAmount The amount to be extracted from the container.
     * @param simulate If true, the container will not be modified.
     * @return The amount of energy that was removed from the container.
     */
    long extractEnergy(long maxAmount, boolean simulate);

    default long internalExtract(long amount, boolean simulate) {
        return extractEnergy(amount, simulate);
    }

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

}
