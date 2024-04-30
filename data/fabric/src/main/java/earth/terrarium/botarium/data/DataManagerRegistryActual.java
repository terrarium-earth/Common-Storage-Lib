package earth.terrarium.botarium.data;

import earth.terrarium.botarium.data.impl.DataManagerRegistryImpl;
import net.msrandom.multiplatform.annotations.Actual;

public interface DataManagerRegistryActual {
    @Actual
    static DataManagerRegistry create(String modid) {
        return new DataManagerRegistryImpl(modid);
    }
}
