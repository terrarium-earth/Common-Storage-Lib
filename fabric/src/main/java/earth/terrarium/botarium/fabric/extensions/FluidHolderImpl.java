package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.fabric.fluid.holder.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(FluidHolder.class)
public class FluidHolderImpl {
    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid) {
        return FabricFluidHolder.of(fluid, FluidConstants.BUCKET, null);
    }

    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid, long amount, CompoundTag tag) {
        return FabricFluidHolder.of(fluid, amount, tag);
    }

    @ImplementsBaseElement
    static FluidHolder fromCompound(CompoundTag tag) {
        FabricFluidHolder fluid = FabricFluidHolder.of(null, 0);
        fluid.deserialize(tag);
        return fluid;
    }

    @ImplementsBaseElement
    static FluidHolder empty() {
        return FabricFluidHolder.empty();
    }
}
