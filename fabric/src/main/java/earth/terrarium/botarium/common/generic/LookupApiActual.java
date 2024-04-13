package earth.terrarium.botarium.common.generic;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

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
