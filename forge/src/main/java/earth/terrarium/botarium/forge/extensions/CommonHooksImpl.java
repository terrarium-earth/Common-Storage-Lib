package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.util.CommonHooks;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.ModList;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.Optional;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @ImplementsBaseElement
    public static int getBurnTime(ItemStack burnable) {
        return ForgeHooks.getBurnTime(burnable, null);
    }
}
