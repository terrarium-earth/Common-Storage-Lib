package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.data.DataManagerRegistry;
import earth.terrarium.botarium.neoforge.data.NeoDataManagerRegistry;
import net.msrandom.extensions.annotations.ClassExtension;

@ClassExtension(DataManagerRegistry.class)
public class DataManagerRegistryImpl {
    static DataManagerRegistry create(String modid) {
        return new NeoDataManagerRegistry(modid);
    }
}
