package testmod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class ForgeTestMod {
    public ForgeTestMod(IEventBus bus) {
        TestMod.init();
    }
}
