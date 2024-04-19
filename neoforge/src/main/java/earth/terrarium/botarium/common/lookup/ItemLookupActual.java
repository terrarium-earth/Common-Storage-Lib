package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.lookup.impl.NeoItemLookup;
import earth.terrarium.botarium.forge.BotariumForge;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface ItemLookupActual {
    @Actual
    static <T, C> ItemLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoItemLookup<T, C> lookup = new NeoItemLookup<>(name, typeClass, contextClass);
        BotariumForge.CAPS.add(lookup);
        return lookup;
    }
}
