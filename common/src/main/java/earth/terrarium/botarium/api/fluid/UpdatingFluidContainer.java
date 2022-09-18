package earth.terrarium.botarium.api.fluid;

import net.minecraft.core.Direction;

public interface UpdatingFluidContainer extends FluidContainer {
    void update();

    @Override
    default UpdatingFluidContainer getContainer(Direction direction) {
        return this;
    }
}
