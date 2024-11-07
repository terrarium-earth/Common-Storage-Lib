package earth.terrarium.common_storage_lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class FabricCommonStorageLib implements ModInitializer {
    
    private boolean loaded = false;
    
    @Override
    public void onInitialize() {
        
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (loaded) return;
            loaded = true;
            CommonStorageLib.init();    // this is required in a later event than initial initialization as registry content might be
                                        // added by other mods at a later point of initialization
        });
        
    }
}
