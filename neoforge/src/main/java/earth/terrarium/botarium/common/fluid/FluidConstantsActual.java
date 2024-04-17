package earth.terrarium.botarium.common.fluid;

import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidConstantsActual {
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
