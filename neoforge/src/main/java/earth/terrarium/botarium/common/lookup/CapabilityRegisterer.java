package earth.terrarium.botarium.common.lookup;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public interface CapabilityRegisterer {
    void register(RegisterCapabilitiesEvent event);

    static void registerAll(RegisterCapabilitiesEvent event, Object... registerers) {
        for (Object registerer : registerers) {
            if (registerer instanceof CapabilityRegisterer capabilityRegisterer) {
                capabilityRegisterer.register(event);
            }
        }
    }
}
