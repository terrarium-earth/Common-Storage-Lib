package earth.terrarium.botarium.resources.item;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.msrandom.multiplatform.annotations.Actual;

public class ItemResourceActual {
    @Actual
    private static ItemResource getCraftingRemainder(ItemResource resource) {
        return ItemResource.of(resource.getCachedStack().getRecipeRemainder());
    }

    @Actual
    private static boolean hasCraftingRemainder(ItemResource resource) {
        return resource.getCachedStack().getRecipeRemainder().isEmpty();
    }
}
