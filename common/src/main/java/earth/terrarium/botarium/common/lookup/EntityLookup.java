package earth.terrarium.botarium.common.lookup;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface EntityLookup<T, C> {

    @Expect
    static <T, C> EntityLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }

    static <T> EntityLookup<T, Void> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Void.class);
    }

    static <T> EntityLookup<T, Direction> createAutomation(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(Entity entity, C context);

    void onRegister(Consumer<EntityRegistrar<T, C>> registrar);

    interface EntityRegistrar<T, C> {
        void register(EntityGetter<T, C> getter, EntityType<?>... containers);
    }

    interface EntityGetter<T, C> {
        T getContainer(Entity entity, C context);

    }
}
