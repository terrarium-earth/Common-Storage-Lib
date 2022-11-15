package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.util.CommonHooks;
import net.minecraftforge.fml.ModList;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
