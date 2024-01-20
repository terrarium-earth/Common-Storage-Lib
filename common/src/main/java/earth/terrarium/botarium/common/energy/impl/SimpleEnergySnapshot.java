package earth.terrarium.botarium.common.energy.impl;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;

public class SimpleEnergySnapshot implements EnergySnapshot {
    private final long energy;

    public SimpleEnergySnapshot(EnergyContainer container) {
        this.energy = container.getStoredEnergy();
    }

    @Override
    public void loadSnapshot(EnergyContainer container) {
        container.setEnergy(energy);
    }
}