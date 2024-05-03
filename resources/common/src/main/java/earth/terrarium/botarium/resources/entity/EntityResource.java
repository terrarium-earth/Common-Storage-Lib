package earth.terrarium.botarium.resources.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.TransferResource;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public final class EntityResource extends TransferResource<EntityType<?>, EntityResource> implements Predicate<EntityResource> {
    public static EntityResource BLANK = EntityResource.of((EntityType<?>) null);

    public static final Codec<EntityResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(EntityResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(EntityResource::getDataPatch)
    ).apply(instance, EntityResource::of));

    public static final Codec<EntityResource> SIMPLE_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(EntityResource::of, EntityResource::getType);

    public static final MapCodec<EntityResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(EntityResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(EntityResource::getDataPatch)
    ).apply(instance, EntityResource::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityResource> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ENTITY_TYPE),
            EntityResource::toHolder,
            DataComponentPatch.STREAM_CODEC,
            EntityResource::getDataPatch,
            EntityResource::of);

    public static EntityResource of(EntityType<?> type) {
        return of(type, DataComponentPatch.EMPTY);
    }

    public static EntityResource of(Holder<EntityType<?>> type) {
        return of(type.value(), DataComponentPatch.EMPTY);
    }

    public static EntityResource of(EntityType<?> type, DataComponentPatch patch) {
        return new EntityResource(type, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public static EntityResource of(Holder<EntityType<?>> type, DataComponentPatch patch) {
        return of(type.value(), patch);
    }

    private EntityResource(@Nullable EntityType<?> type, PatchedDataComponentMap components) {
        super(type, components);
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

    public Holder<EntityType<?>> toHolder() {
        return type.builtInRegistryHolder();
    }
}
