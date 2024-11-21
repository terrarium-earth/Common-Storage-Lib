package earth.terrarium.common_storage_lib;

import net.fabricmc.api.ModInitializer;

public class FabricCommonStorageLib implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        CommonStorageLib.init();
        
    }
}
