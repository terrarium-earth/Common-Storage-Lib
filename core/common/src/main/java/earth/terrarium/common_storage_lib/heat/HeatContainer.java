package earth.terrarium.common_storage_lib.heat;

public interface HeatContainer {
    double getAmbientTemp();

    double getThermalCapacity();

    double getThermalResistance();

    double getThermalConductivity();

    double getTemp();

    double setTemp(double temperature, boolean simulate);

    double fluctuateTemp(double amount, boolean simulate);
}
