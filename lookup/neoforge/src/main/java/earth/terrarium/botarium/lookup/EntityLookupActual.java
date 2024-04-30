package earth.terrarium.botarium.lookup;

import earth.terrarium.botarium.common.lookup.impl.NeoEntityLookup;
import earth.terrarium.botarium.forge.BotariumForge;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

public interface EntityLookupActual {
    @Actual
    static <T, C> EntityLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        NeoEntityLookup<T, C> lookup = new NeoEntityLookup<>(name, typeClass, contextClass);
        BotariumForge.CAPS.add(lookup);
        return lookup;
    }
}
