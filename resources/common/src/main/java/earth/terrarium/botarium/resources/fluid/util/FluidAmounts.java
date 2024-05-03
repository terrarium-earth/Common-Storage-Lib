package earth.terrarium.botarium.resources.fluid.util;

import net.msrandom.multiplatform.annotations.Expect;

@SuppressWarnings("ALL")
public final class FluidAmounts {
    private FluidAmounts() {
    }

    @Expect
    public static long toPlatformAmount(long millibuckets);

    @Expect
    public static long toMillibuckets(long platformAmount);

    @Expect
    public static final long BUCKET;

    @Expect
    public static final long BOTTLE;

    @Expect
    public static final long BLOCK;

    @Expect
    public static final long INGOT;

    @Expect
    public static final long NUGGET;
}