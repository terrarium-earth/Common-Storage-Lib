package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Updatable;

public interface EnergyBlock extends Updatable {

    /**
     * @return The {@link StatefulEnergyContainer} for the block.
     */
    StatefulEnergyContainer getEnergyStorage();
}
