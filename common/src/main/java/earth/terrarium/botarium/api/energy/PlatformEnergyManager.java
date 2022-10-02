package earth.terrarium.botarium.api.energy;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface PlatformEnergyManager {

    /**
     * @return The current amount of stored energy.
     */
    long getStoredEnergy();

    /**
     * @return The maximum amount of energy that can be stored.
     */
    long getCapacity();

    /**
     * Extracts an amount of energy from the manager.
     * @param amount The amount of energy to extract.
     * @param simulate If true, the extraction will only be simulated.
     * @return The amount of energy that was extracted.
     */
    long extract(long amount, boolean simulate);

    /**
     * Inserts an amount of energy from the manager.
     * @param amount The amount of energy to insert.
     * @param simulate If true, the insertion will only be simulated.
     * @return The amount of energy that was inserted.
     */
    long insert(long amount, boolean simulate);
}
