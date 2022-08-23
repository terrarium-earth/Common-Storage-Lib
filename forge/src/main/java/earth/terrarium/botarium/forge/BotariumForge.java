package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import net.minecraftforge.fml.common.Mod;

@Mod(Botarium.MOD_ID)
public class BotariumForge {
    public BotariumForge() {
        Botarium.init();
    }
}