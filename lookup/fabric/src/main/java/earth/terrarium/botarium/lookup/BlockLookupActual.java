package earth.terrarium.botarium.lookup;

import earth.terrarium.botarium.lookup.impl.FabricBlockLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface BlockLookupActual {
    @Actual
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricBlockLookup<>(name, typeClass, contextClass);
    }
}
