package earth.terrarium.botarium.lookup;

import earth.terrarium.botarium.common.lookup.impl.NeoBlockLookup;
import earth.terrarium.botarium.forge.BotariumForge;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface BlockLookupActual {
    @Actual
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoBlockLookup<T, C> lookup = new NeoBlockLookup<>(name, typeClass, contextClass);
        BotariumForge.CAPS.add(lookup);
        return lookup;
    }
}
