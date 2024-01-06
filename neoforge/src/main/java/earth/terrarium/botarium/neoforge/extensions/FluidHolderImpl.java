package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.fluids.FluidType;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(FluidHolder.class)
public class FluidHolderImpl {
    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid) {
        return new ForgeFluidHolder(fluid, FluidType.BUCKET_VOLUME, null);
    }

    @ImplementsBaseElement
    static FluidHolder of(Fluid fluid, long amount, CompoundTag tag) {
        return new ForgeFluidHolder(fluid, (int) amount, tag);
    }

    @ImplementsBaseElement
    static FluidHolder fromCompound(CompoundTag tag) {
        return ForgeFluidHolder.fromCompound(tag);
    }

    @ImplementsBaseElement
    static FluidHolder empty() {
        return ForgeFluidHolder.empty();
    }
}
