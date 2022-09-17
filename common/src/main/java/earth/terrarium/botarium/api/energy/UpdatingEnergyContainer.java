package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;

public interface UpdatingEnergyContainer extends EnergyContainer {
    void update();
    EnergySnapshot createSnapshot();
    void readSnapshot(EnergySnapshot snapshot);

    default UpdatingEnergyContainer getContainer(Direction direction) {
        return this;
    }
}
