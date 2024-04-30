package earth.terrarium.botarium.lookup;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.ArrayList;
import java.util.List;

public interface RegistryEventListener {
    List<RegistryEventListener> REGISTRARS = new ArrayList<>();

    static void registerAll(RegisterCapabilitiesEvent event) {
        REGISTRARS.forEach(registerer -> registerer.register(event));
    }

    void register(RegisterCapabilitiesEvent event);

    default void registerSelf() {
        REGISTRARS.add(this);
    }
}
