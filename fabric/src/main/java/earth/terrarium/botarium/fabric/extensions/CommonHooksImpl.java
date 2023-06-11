package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.util.CommonHooks;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.ItemStack;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @ImplementsBaseElement
    public static int getBurnTime(ItemStack burnable) {
        Integer burnTime = FuelRegistry.INSTANCE.get(burnable.getItem());
        return burnTime == null ? 0 : burnTime;
    }
}
