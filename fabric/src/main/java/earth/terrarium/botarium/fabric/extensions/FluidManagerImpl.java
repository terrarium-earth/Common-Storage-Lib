package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.fabric.fluid.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(FluidHooks.class)
@SuppressWarnings("UnstableApiUsage")
public class FluidManagerImpl {

    @ImplementsBaseElement
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, CompoundTag tag) {
        return FabricFluidHolder.of(fluid, tag, amount);
    }

    @ImplementsBaseElement
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        FabricFluidHolder fluid = FabricFluidHolder.of(null, 0);
        fluid.deserialize(compoundTag);
        return fluid;
    }

    @ImplementsBaseElement
    public static FluidHolder emptyFluid() {
        return FabricFluidHolder.EMPTY;
    }

    @ImplementsBaseElement
    public static long buckets(int buckets) {
        return FluidConstants.BUCKET * buckets;
    }

    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount / 81;
    }

    @ImplementsBaseElement
    private static long getBucketAmount() {
        return FluidConstants.BUCKET;
    }

    @ImplementsBaseElement
    private static long getBottleAmount() {
        return FluidConstants.BOTTLE;
    }

    @ImplementsBaseElement
    private static long getBlockAmount() {
        return FluidConstants.BLOCK;
    }

    @ImplementsBaseElement
    private static long getIngotAmount() {
        return FluidConstants.INGOT;
    }

    @ImplementsBaseElement
    private static long getNuggetAmount() {
        return FluidConstants.NUGGET;
    }
}
