package earth.terrarium.botarium.common.data.utils;

public interface ContainerSerializer<C, D> {
    D captureData(C container);

    void applyData(C container, D snapshot);
}
