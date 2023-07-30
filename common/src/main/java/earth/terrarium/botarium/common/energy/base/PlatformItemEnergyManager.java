package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import org.jetbrains.annotations.ApiStatus;

/**
 * @deprecated Use {@link EnergyApi#getItemEnergyContainer(ItemStackHolder)} instead.
 */
@ApiStatus.NonExtendable
@Deprecated
public interface PlatformItemEnergyManager {

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
     *
     * @param amount   The amount of energy to extract.
     * @param simulate If true, the extraction will only be simulated.
     * @return The amount of energy that was extracted.
     */
    long extract(ItemStackHolder holder, long amount, boolean simulate);

    /**
     * Inserts an amount of energy from the manager.
     *
     * @param amount   The amount of energy to insert.
     * @param simulate If true, the insertion will only be simulated.
     * @return The amount of energy that was inserted.
     */
    long insert(ItemStackHolder holder, long amount, boolean simulate);

    /**
     * @return If the manager supports insertion.
     */
    boolean supportsInsertion();

    /**
     * @return If the manager supports extraction.
     */
    boolean supportsExtraction();
}
