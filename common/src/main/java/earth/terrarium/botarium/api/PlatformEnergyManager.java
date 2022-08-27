package earth.terrarium.botarium.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface PlatformEnergyManager {
    long getCapacity();
    long maxCapacity();
    long extract(int amount, boolean simulate);
    long insert(int amount, boolean simulate);
}
