package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.data.DataManagerRegistry;
import earth.terrarium.botarium.fabric.data.FabricDataManagerRegistry;
import net.msrandom.extensions.annotations.ClassExtension;

@ClassExtension(DataManagerRegistry.class)
public class DataManagerRegistryImpl {
    static DataManagerRegistry create(String modid) {
        return new FabricDataManagerRegistry(modid);
    }
}
