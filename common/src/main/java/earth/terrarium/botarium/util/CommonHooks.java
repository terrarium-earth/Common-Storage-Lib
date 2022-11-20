package earth.terrarium.botarium.util;

import net.minecraft.world.item.ItemStack;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class CommonHooks {

    @ImplementedByExtension
    public static boolean isModLoaded(String modid) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static int getBurnTime(ItemStack burnable) {
        throw new NotImplementedException();
    }
}
