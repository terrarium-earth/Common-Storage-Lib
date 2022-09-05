package earth.terrarium.botarium.api.energy;

public interface UpdatingEnergyContainer extends EnergyContainer {
    void update();
    EnergySnapshot createSnapshot();
    void readSnapshot(EnergySnapshot snapshot);
}
