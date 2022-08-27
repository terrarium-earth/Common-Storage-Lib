package earth.terrarium.botarium.api;

public interface EnergyMarker<T> {
    EnergyHoldable getEnergyStorage(T object);
}
