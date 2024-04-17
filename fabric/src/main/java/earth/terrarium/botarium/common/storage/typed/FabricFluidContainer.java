package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.world.level.material.Fluid;

public class FabricFluidContainer extends FabricWrappedContainer<Fluid, FluidUnit, FluidVariant, FluidHolder, FluidContainer>{
    public FabricFluidContainer(FluidContainer container) {
        super(container);
    }

    @Override
    public FluidUnit fromVariant(FluidVariant variant) {
        return FluidUnit.of(variant.getFluid(), variant.getComponents());
    }

    @Override
    public FluidVariant toVariant(FluidUnit unit) {
        return FluidVariant.of(unit.unit(), unit.components());
    }
}
