package earth.terrarium.botarium.api.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class FluidHooks {

    @ImplementedByExtension
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, CompoundTag tag) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static FluidHolder emptyFluid() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static long buckets(int buckets) {
        return 0;
    }
}
