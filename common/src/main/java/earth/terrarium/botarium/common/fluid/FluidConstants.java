package earth.terrarium.botarium.common.fluid;

import net.msrandom.multiplatform.annotations.Expect;

@Expect
public class FluidConstants {

    /**
     * Converts the given amount of millibuckets to the platform-specific amount of fluid.
     *
     * @param millibuckets The amount of millibuckets to convert.
     * @return The converted value as a long.
     */
    public static long toPlatformAmount(long millibuckets);

    /**
     * Converts the given amount of platform-specific amount of fluid to millibuckets.
     *
     * @param platformAmount The amount of platform specific liquid to convert.
     * @return The converted millibuckets as a long.
     */
    public static long toMillibuckets(long platformAmount);

    /**
     * @return The amount of fluid a bucket is for the platform.
     */
    public static final long BUCKET;

    /**
     * @return The amount of fluid a bottle is for the platform.
     */
    public static final long BOTTLE;

    /**
     * @return The amount of fluid a block is for the platform.
     */
    public static final long BLOCK;

    /**
     * @return The amount of fluid an ingot is for the platform.
     */
    public static final long INGOT;

    /**
     * @return The amount of fluid a nugget is for the platform.
     */
    public static final long NUGGET;
}