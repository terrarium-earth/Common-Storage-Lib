package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.ContainerApi;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.fabric.generic.FabricBlockContainerLookup;
import earth.terrarium.botarium.fabric.generic.FabricEntityContainerLookup;
import earth.terrarium.botarium.fabric.generic.FabricItemContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(ContainerApi.class)
public class ContainerApiImpl {
    @ImplementsBaseElement
    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricBlockContainerLookup<>(name, typeClass, contextClass);
    }

    @ImplementsBaseElement
    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricItemContainerLookup<>(name, typeClass, contextClass);
    }

    @ImplementsBaseElement
    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return new FabricEntityContainerLookup<>(name, typeClass, contextClass);
    }
}
