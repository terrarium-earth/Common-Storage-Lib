package earth.terrarium.common_storage_lib.lookup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("common_storage_lib_lookup")
public class NeoLookupLib {
    public NeoLookupLib() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryEventListener.registerAll(event);
    }
}
