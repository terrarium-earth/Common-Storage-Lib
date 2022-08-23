package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import net.fabricmc.api.ModInitializer;

public class BotariumFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Botarium.init();
    }
}