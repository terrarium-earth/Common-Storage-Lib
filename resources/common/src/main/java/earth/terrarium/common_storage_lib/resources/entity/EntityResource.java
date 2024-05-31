package earth.terrarium.common_storage_lib.resources.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.common_storage_lib.resources.ResourceComponent;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public final class EntityResource extends ResourceComponent {
    public static EntityResource BLANK = EntityResource.of((EntityType<?>) null);

    public static final Codec<EntityResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(EntityResource::getType),
            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(EntityResource::getTag)
    ).apply(instance, EntityResource::of));

    public static final Codec<EntityResource> SIMPLE_CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(EntityResource::of, EntityResource::getType);

    public static final MapCodec<EntityResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(EntityResource::getType),
            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(EntityResource::getTag)
    ).apply(instance, EntityResource::of));

    public static EntityResource of(EntityType<?> type) {
        return of(type, null);
    }

    public static EntityResource of(Holder<EntityType<?>> type) {
        return of(type.value());
    }

    public static EntityResource of(EntityType<?> type, CompoundTag tag) {
        return new EntityResource(type, tag);
    }

    public static EntityResource of(Holder<EntityType<?>> type, CompoundTag tag) {
        return of(type.value(), tag);
    }

    public static EntityResource ofEntity(Entity entity) {
        return of(entity.getType(), entity.saveWithoutId(new CompoundTag()));
    }

    public static EntityResource ofEntityWithoutData(Entity entity) {
        return of(entity.getType(), null);
    }

    private final EntityType<?> type;

    private EntityResource(@Nullable EntityType<?> type, CompoundTag tag) {
        super(tag);
        this.type = type;
    }

    public boolean isOf(EntityType<?> type) {
        return this.type == type;
    }

    @Override
    public boolean isBlank() {
        return type == null;
    }

    @Nullable
    public EntityType<?> getType() {
        return type;
    }

    public <T> EntityResource set(Codec<T> codec, String key, T value) {
        CompoundTag tag = getTag();
        if (tag == null) {
            tag = new CompoundTag();
        } else {
            tag = tag.copy();
        }
        tag.put(key, codec.encodeStart(NbtOps.INSTANCE, value).result().orElseThrow());
        return new EntityResource(type, tag);
    }

    public EntityResource remove(String key) {
        CompoundTag tag = getTag();
        if (tag == null) return this;
        tag = tag.copy();
        tag.remove(key);
        return new EntityResource(type, tag);
    }

    public ResourceStack<EntityResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    public Holder<EntityType<?>> toHolder() {
        return type.builtInRegistryHolder();
    }
}
