package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.ContainerApi;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.neoforge.generic.NeoForgeCapsHandler;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(ContainerApi.class)
public class ContainerApiImpl {
    @ImplementedByExtension
    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerBlockLookup(name, typeClass, contextClass);
    }

    @ImplementedByExtension
    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerItemLookup(name, typeClass, contextClass);
    }

    @ImplementedByExtension
    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return NeoForgeCapsHandler.registerEntityLookup(name, typeClass, contextClass);
    }
}
