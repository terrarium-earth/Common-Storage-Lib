package earth.terrarium.botarium.neoforge.extensions;

import net.minecraft.world.item.ItemStack;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.CommonHooks;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @ImplementsBaseElement
    public static int getBurnTime(ItemStack burnable) {
        return CommonHooks.getBurnTime(burnable, null);
    }
}
