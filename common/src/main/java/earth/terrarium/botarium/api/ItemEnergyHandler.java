package earth.terrarium.botarium.api;

public interface ItemEnergyHandler {
    long getCapacity();
    long maxCapacity();
    long extract(int amount, boolean simulate);
    long insert(int amount, boolean simulate);
}
