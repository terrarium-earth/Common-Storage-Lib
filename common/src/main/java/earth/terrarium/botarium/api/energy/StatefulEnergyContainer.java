package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface StatefulEnergyContainer extends EnergyContainer {

    /**
     * Called when the operation has been completed and the data has been updated.
     */
    void update();

    /**
     * @param direction The {@link Direction} to get the container from.
     * @return A {@link StatefulEnergyContainer} for a given {@link Direction}.
     */
    default StatefulEnergyContainer getContainer(Direction direction) {
        return this;
    }
}
