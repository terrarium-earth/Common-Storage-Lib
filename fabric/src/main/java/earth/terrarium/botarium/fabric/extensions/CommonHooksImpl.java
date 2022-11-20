package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.util.CommonHooks;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.ItemStack;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

@ClassExtension(CommonHooks.class)
public class CommonHooksImpl {

    @ImplementsBaseElement
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @ImplementsBaseElement
    public static int getBurnTime(ItemStack burnable) {
        return Optional.of(FuelRegistry.INSTANCE.get(burnable.getItem())).orElse(0);
    }
}
