package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.Updatable;

public interface EnergyBlock extends Updatable {
    StatefulEnergyContainer getEnergyStorage();
}
