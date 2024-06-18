package earth.terrarium.common_storage_lib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CommonStorageLib.MOD_ID)
public class NeoCommonStorageLib {
    public NeoCommonStorageLib(IEventBus bus) {
        CommonStorageLib.init();
    }
}
