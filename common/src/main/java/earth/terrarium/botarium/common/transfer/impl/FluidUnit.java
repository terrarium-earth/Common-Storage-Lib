package earth.terrarium.botarium.common.transfer.impl;

import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record FluidUnit(Fluid unit, DataComponentPatch components) implements TransferUnit<Fluid> {
    public static final FluidUnit BLANK = new FluidUnit(Fluids.EMPTY, DataComponentPatch.EMPTY);

    public static FluidUnit of(Fluid fluid) {
        return new FluidUnit(fluid, DataComponentPatch.EMPTY);
    }

    public static FluidUnit of(Fluid fluid, DataComponentPatch components) {
        return new FluidUnit(fluid, components);
    }

    public static FluidUnit of(FluidHolder holder) {
        return new FluidUnit(holder.getFluid(), holder.getPatch());
    }

    @Override
    public boolean isBlank() {
        return unit == Fluids.EMPTY;
    }

    public boolean matches(FluidHolder holder) {
        return isOf(holder.getFluid()) && componentsMatch(holder.getPatch());
    }

    public FluidHolder toHolder(long amount) {
        return FluidHolder.of(unit, amount, components);
    }

    public FluidHolder toHolder() {
        return toHolder(FluidConstants.BUCKET);
    }
}
