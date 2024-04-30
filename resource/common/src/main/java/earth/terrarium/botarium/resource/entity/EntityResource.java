package earth.terrarium.botarium.resource.entity;

import earth.terrarium.botarium.resource.ResourceStack;
import earth.terrarium.botarium.resource.TransferResource;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityResource implements TransferResource<EntityType<?>, EntityResource> {
    public static EntityResource BLANK = EntityResource.of(null, DataComponentPatch.EMPTY);

    @Nullable
    private final EntityType<?> type;
    private final PatchedDataComponentMap components;

    public static EntityResource of(EntityType<?> type) {
        return of(type, DataComponentPatch.EMPTY);
    }

    public static EntityResource of(EntityType<?> type, DataComponentPatch patch) {
        return new EntityResource(type, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    private EntityResource(@Nullable EntityType<?> type, PatchedDataComponentMap components) {
        this.type = type;
        this.components = components;
    }

    @Override
    public boolean isBlank() {
        return type == null;
    }

    @Nullable
    @Override
    public EntityType<?> getType() {
        return type;
    }

    @Override
    public DataComponentPatch getDataPatch() {
        return components.asPatch();
    }

    @Override
    public <D> EntityResource set(DataComponentType<D> type, D value) {
        PatchedDataComponentMap newComponents = components.copy();
        newComponents.set(type, value);
        return new EntityResource(this.type, newComponents);
    }

    @Override
    public EntityResource modify(DataComponentPatch patch) {
        PatchedDataComponentMap newComponents = components.copy();
        newComponents.applyPatch(patch);
        return new EntityResource(type, newComponents);
    }

    @Override
    public ResourceStack<EntityResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    @NotNull
    @Override
    public DataComponentMap getComponents() {
        return components;
    }
}
