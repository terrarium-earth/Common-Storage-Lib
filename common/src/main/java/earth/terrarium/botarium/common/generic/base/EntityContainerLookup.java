package earth.terrarium.botarium.common.generic.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public interface EntityContainerLookup<T, C> {

    /**
     * @return The {@link T} for the block.
     */
    T getContainer(Entity entity, C context);

    void registerEntityTypes(EntityGetter<T, C> getter, Supplier<EntityType<?>>... containers);

    interface EntityGetter<T, C> {
        T getContainer(Entity entity, C context);
    }
}
