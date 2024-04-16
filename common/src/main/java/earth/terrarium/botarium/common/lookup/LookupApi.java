package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.base.BlockContainerLookup;
import earth.terrarium.botarium.common.lookup.base.EntityContainerLookup;
import earth.terrarium.botarium.common.lookup.base.ItemContainerLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

public class LookupApi {

    @Expect
    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    public static <T> BlockContainerLookup<T, @Nullable Direction> createBlockLookup(ResourceLocation name, Class<T> typeClass) {
        return createBlockLookup(name, typeClass, Direction.class);
    }

    @Expect
    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    public static <T> ItemContainerLookup<T, ItemContext> createItemLookup(ResourceLocation name, Class<T> typeClass) {
        return createItemLookup(name, typeClass, ItemContext.class);
    }

    @Expect
    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    public static <T> EntityContainerLookup<T, Void> createEntityLookup(ResourceLocation name, Class<T> typeClass) {
        return createEntityLookup(name, typeClass, Void.class);
    }

    public static <T> EntityContainerLookup<T, @Nullable Direction> createAutomationEntityLookup(ResourceLocation name, Class<T> typeClass) {
        return createEntityLookup(name, typeClass, Direction.class);
    }
}
