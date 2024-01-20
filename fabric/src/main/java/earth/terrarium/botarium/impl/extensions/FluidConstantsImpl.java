package earth.terrarium.botarium.impl.extensions;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(earth.terrarium.botarium.common.fluid.FluidConstants.class)
public class FluidConstantsImpl {

    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount / 81;
    }

    @ImplementsBaseElement
    public static long fromMillibuckets(long amount) {
        return amount * 81;
    }

    @ImplementsBaseElement
    public static long getBucketAmount() {
        return FluidConstants.BUCKET;
    }

    @ImplementsBaseElement
    public static long getBottleAmount() {
        return FluidConstants.BOTTLE;
    }

    @ImplementsBaseElement
    public static long getBlockAmount() {
        return FluidConstants.BLOCK;
    }

    @ImplementsBaseElement
    public static long getIngotAmount() {
        return FluidConstants.INGOT;
    }

    @ImplementsBaseElement
    public static long getNuggetAmount() {
        return FluidConstants.NUGGET;
    }
}
