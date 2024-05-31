package earth.terrarium.botarium.lookup;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("botarium_lookup")
public class NeoBotariumLookup {
    public NeoBotariumLookup() {
        IEventBus event = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryEventListener.registerAll(event);
    }
}
