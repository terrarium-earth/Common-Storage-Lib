package earth.terrarium.botarium.resources.fluid.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class FluidAmountsActual {
    @Actual
    public static long toPlatformAmount(long millibuckets) {
        return FluidConstants.fromBucketFraction(millibuckets, 1000);
    }

    @Actual
    public static long toMillibuckets(long platformAmount) {
        return platformAmount * 1000 / FluidConstants.BUCKET;
    }

    @Actual
    public static final long BUCKET = FluidConstants.BUCKET;

    @Actual
    public static final long BOTTLE = FluidConstants.BOTTLE;

    @Actual
    public static final long BLOCK = FluidConstants.BUCKET;

    @Actual
    public static final long INGOT = FluidConstants.INGOT;

    @Actual
    public static final long NUGGET = FluidConstants.NUGGET;
}
