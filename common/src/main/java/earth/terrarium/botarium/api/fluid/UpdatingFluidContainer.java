package earth.terrarium.botarium.api.fluid;

import net.minecraft.core.Direction;

public interface UpdatingFluidContainer<T> extends FluidContainer {
    void update(T updatable);

    @Override
    default UpdatingFluidContainer getContainer(Direction direction) {
        return this;
    }
}
