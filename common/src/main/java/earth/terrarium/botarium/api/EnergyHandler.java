package earth.terrarium.botarium.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

public class EnergyHandler {

    @ExpectPlatform
    public static ItemEnergyHandler getItemHandler(ItemStack stack) {
        throw new NotImplementedException("Energy Handler not Implemented");
    }

    @ExpectPlatform
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }
}
