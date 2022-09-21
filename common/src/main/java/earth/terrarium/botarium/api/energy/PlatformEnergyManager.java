package earth.terrarium.botarium.api.energy;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface PlatformEnergyManager {
    long getStoredEnergy();
    long getCapacity();
    long extract(long amount, boolean simulate);
    long insert(long amount, boolean simulate);
}
