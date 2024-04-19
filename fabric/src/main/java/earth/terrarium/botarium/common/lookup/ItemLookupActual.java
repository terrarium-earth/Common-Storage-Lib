package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.lookup.impl.FabricItemLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface ItemLookupActual {
    @Actual
    static <T, C> ItemLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricItemLookup<>(name, typeClass, contextClass);
    }
}
