package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;

public interface StatefulEnergyContainer extends EnergyContainer {
    void update();

    default StatefulEnergyContainer getContainer(Direction direction) {
        return this;
    }
}
