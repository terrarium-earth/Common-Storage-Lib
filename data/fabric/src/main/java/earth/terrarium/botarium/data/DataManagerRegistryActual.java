package earth.terrarium.botarium.data;

import earth.terrarium.botarium.data.impl.DataManagerBuilderImpl;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Supplier;

@Actual
public final class DataManagerRegistryActual {
    private final String modid;

    @Actual
    private DataManagerRegistryActual(String modid) {
        this.modid = modid;
    }

    @Actual
    public static DataManagerRegistry create(String modid) {
        return new DataManagerRegistry(modid);
    }

    @Actual
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(modid, factory);
    }

    @Actual
    public void init() {
    }
}
