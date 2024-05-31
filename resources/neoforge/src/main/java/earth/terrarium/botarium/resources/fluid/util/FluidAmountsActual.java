package earth.terrarium.botarium.resources.fluid.util;

import net.minecraftforge.fluids.FluidType;
import net.msrandom.multiplatform.annotations.Actual;

public class FluidAmountsActual {
    @Actual
    public static long toPlatformAmount(long millibuckets) {
        return millibuckets;
    }

    @Actual
    public static long toMillibuckets(long platformAmount) {
        return platformAmount;
    }

    @Actual
    public static final long BUCKET = FluidType.BUCKET_VOLUME;

    @Actual
    public static final long BOTTLE = FluidType.BUCKET_VOLUME / 4;

    @Actual
    public static final long BLOCK = 810;

    @Actual
    public static final long INGOT = 90;

    @Actual
    public static final long NUGGET = 10;
}
