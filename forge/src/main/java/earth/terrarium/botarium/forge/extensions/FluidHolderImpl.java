package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(FluidHolder.class)
public class FluidHolderImpl {
    @ImplementsBaseElement
    public static FluidHolder of(Fluid fluid) {
        return new ForgeFluidHolder(fluid, FluidType.BUCKET_VOLUME, null);
    }

    @ImplementsBaseElement
    public static FluidHolder of(Fluid fluid, long amount, CompoundTag tag) {
        return new ForgeFluidHolder(fluid, (int) amount, tag);
    }

    @ImplementsBaseElement
    public static FluidHolder fromCompound(CompoundTag tag) {
        return ForgeFluidHolder.fromCompound(tag);
    }

    @ImplementsBaseElement
    public static FluidHolder empty() {
        return ForgeFluidHolder.empty();
    }
}
