package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class CommonFluidContainer extends CommonWrappedContainer<Fluid, FluidUnit, FluidVariant, FluidHolder> implements FluidContainer {
    public CommonFluidContainer(Storage<FluidVariant> storage) {
        super(storage);
    }

    @Override
    public FluidUnit toUnit(FluidVariant variant) {
        return FluidUnit.of(variant.getFluid(), variant.getComponents());
    }

    @Override
    public FluidVariant toVariant(FluidUnit unit) {
        return FluidVariant.of(unit.unit(), unit.components());
    }

    @Override
    public FluidHolder createHolder(@Nullable FluidUnit unit, long amount) {
        return unit == null || amount == 0 ? FluidHolder.EMPTY : unit.toHolder(amount);
    }
}
