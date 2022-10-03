package earth.terrarium.botarium.api.fluid;

import net.minecraft.core.Direction;

import org.jetbrains.annotations.Nullable;

public interface UpdatingFluidContainer extends FluidContainer {
    void update();

    @Override default UpdatingFluidContainer getContainer(@Nullable Direction direction) {
        return this;
    }
}
