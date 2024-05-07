package earth.terrarium.botarium.resources.item;

import net.msrandom.multiplatform.annotations.Actual;

public class ItemResourceActual {
    @Actual
    private static ItemResource getCraftingRemainder(ItemResource resource) {
        return ItemResource.of(resource.getCachedStack().getCraftingRemainingItem());
    }

    @Actual
    private static boolean hasCraftingRemainingItem(ItemResource resource) {
        return resource.getCachedStack().hasCraftingRemainingItem();
    }
}
