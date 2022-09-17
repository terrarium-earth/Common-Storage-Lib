package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;

public interface StatefulEnergyContainer extends EnergyContainer {
    void update();
    EnergySnapshot createSnapshot();

    default void readSnapshot(EnergySnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }

    default StatefulEnergyContainer getContainer(Direction direction) {
        return this;
    }
}
