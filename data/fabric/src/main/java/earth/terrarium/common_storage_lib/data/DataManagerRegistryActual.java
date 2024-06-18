package earth.terrarium.common_storage_lib.data;

import earth.terrarium.common_storage_lib.data.impl.DataManagerBuilderImpl;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Supplier;

@Actual
public final class DataManagerRegistryActual {
    private final String modid;

    @Actual
    public DataManagerRegistryActual(String modid) {
        this.modid = modid;
    }

    @Actual
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(modid, factory);
    }

    @Actual
    public void init() {
    }
}
