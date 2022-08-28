package earth.terrarium.botarium.api;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface PlatformEnergyManager {
    long getStoredEnergy();
    long getCapacity();
    long extract(int amount, boolean simulate);
    long insert(int amount, boolean simulate);
}
