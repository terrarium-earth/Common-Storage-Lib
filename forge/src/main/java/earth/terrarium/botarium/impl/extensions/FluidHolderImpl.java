package earth.terrarium.botarium.impl.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.impl.fluid.ForgeFluidHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(FluidHolder.class)
public interface FluidHolderImpl {
    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid, long amount, CompoundTag tag) {
        return new ForgeFluidHolder(fluid, (int) amount, tag);
    }

    @ImplementsBaseElement
    static FluidHolder empty() {
        return ForgeFluidHolder.empty();
    }
}
