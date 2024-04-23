package earth.terrarium.botarium.common.heat;

public interface HeatContainer {
    double getAmbientTemp();

    double getThermalCapacity();

    double getThermalResistance();

    double getThermalConductivity();

    double getTemp();

    double setTemp(double temperature, boolean simulate);

    double fluctuateTemp(double amount, boolean simulate);
}
