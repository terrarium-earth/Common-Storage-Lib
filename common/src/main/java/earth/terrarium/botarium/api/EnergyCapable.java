package earth.terrarium.botarium.api;

public interface EnergyCapable<T> {
    EnergyContainer getEnergyStorage(T object);
}
