package earth.terrarium.botarium.api.fluid;

public interface StatefulFluidContainer extends FluidContainer {
    void update();
    FluidSnapshot createSnapshot();
    default void readSnapshot(FluidSnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }
}
