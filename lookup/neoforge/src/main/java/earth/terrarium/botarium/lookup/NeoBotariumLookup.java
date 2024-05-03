package earth.terrarium.botarium.lookup;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod("botarium_lookup")
public class NeoBotariumLookup {
    public NeoBotariumLookup(IEventBus event) {
        event.addListener(RegistryEventListener::registerAll);
    }
}
