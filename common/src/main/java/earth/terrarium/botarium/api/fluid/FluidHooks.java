package earth.terrarium.botarium.api.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class FluidHooks {
    public static final long BUCKET = 81000;
    public static final long BOTTLE = 27000;
    public static final long BLOCK = 81000;
    public static final long INGOT = 9000;
    public static final long NUGGET = 1000;

    @ImplementedByExtension
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, @Nullable CompoundTag tag) {
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
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static long toMillibuckets(long amount) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBucketAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBottleAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBlockAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getIngotAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getNuggetAmount() {
        throw new NotImplementedException();
    }
}
