package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.util.CommonHooks;
import net.fabricmc.loader.api.FabricLoader;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
