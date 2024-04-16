package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.lookup.base.BlockContainerLookup;
import earth.terrarium.botarium.common.lookup.base.EntityContainerLookup;
import earth.terrarium.botarium.common.lookup.base.ItemContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public class LookupApiActual {
    @Actual
    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }

    @Actual
    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }

    @Actual
    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }
}
