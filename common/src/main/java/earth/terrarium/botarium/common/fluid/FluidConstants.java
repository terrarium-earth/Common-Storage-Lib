package earth.terrarium.botarium.common.fluid;

import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

public class FluidConstants {
    /**
     * Converts the given amount of millibuckets to the platform-specific amount of fluid.
     *
     * @param millibuckets The amount of millibuckets to convert.
     * @return The converted value as a long.
     */
    @ImplementsBaseElement
    public static long fromMillibuckets(long millibuckets) {
        throw new NotImplementedException();
    }

    /**
     * Converts the given amount of platform-specific amount of fluid to millibuckets.
     *
     * @param amount The amount of platform specific liquid to convert.
     * @return The converted millibuckets as a long.
     */
    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a bucket is for the platform.
     */
    @ImplementedByExtension
    public static long getBucketAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a bottle is for the platform.
     */
    @ImplementedByExtension
    public static long getBottleAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a block is for the platform.
     */
    @ImplementedByExtension
    public static long getBlockAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid an ingot is for the platform.
     */
    @ImplementedByExtension
    public static long getIngotAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a nugget is for the platform.
     */
    @ImplementedByExtension
    public static long getNuggetAmount() {
        throw new NotImplementedException();
    }
}
