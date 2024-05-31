package earth.terrarium.common_storage_lib.lookup;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod("common_storage_lib_lookup")
public class NeoLookupLib {
    public NeoLookupLib(IEventBus event) {
        event.addListener(RegistryEventListener::registerAll);
    }
}
