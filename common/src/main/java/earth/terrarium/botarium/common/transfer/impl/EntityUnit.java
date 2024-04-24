package earth.terrarium.botarium.common.transfer.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record EntityUnit(@Nullable EntityType<?> unit, DataComponentPatch components) implements TransferUnit<EntityType<?>> {
    public static final EntityUnit BLANK = new EntityUnit(null, DataComponentPatch.EMPTY);

    public static final Codec<EntityUnit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(EntityUnit::type),
            CompoundTag.CODEC.optionalFieldOf("entityData", new CompoundTag()).forGetter(EntityUnit::getEntityData)
    ).apply(instance, EntityUnit::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, EntityUnit> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, EntityUnit>() {
        @Override
        public EntityUnit decode(RegistryFriendlyByteBuf object) {

        }

        @Override
        public void encode(RegistryFriendlyByteBuf object, EntityUnit object2) {

        }
    };

    public static EntityUnit of(EntityType<?> unit, CompoundTag tag) {
        return new EntityUnit(unit, DataComponentPatch.builder().set(DataComponents.ENTITY_DATA, CustomData.of(tag)).build());
    }

    public static EntityUnit ofDataless(Entity entity) {
        return new EntityUnit(entity.getType(), DataComponentPatch.EMPTY);
    }

    public static EntityUnit of(Entity entity) {
        return of(entity.getType(), entity.saveWithoutId(new CompoundTag()));
    }

    @Override
    public boolean isBlank() {
        return unit == null;
    }

    public CompoundTag getEntityData() {
        Optional<? extends CustomData> customData = components.get(DataComponents.ENTITY_DATA);
        if (customData != null && customData.isPresent()) {
            return customData.get().copyTag();
        } else {
            return new CompoundTag();
        }
    }

    public Entity create(Level level) {
        if (isBlank()) {
            return null;
        }
        Entity entity = unit.create(level);
        Optional<? extends CustomData> customData = components.get(DataComponents.ENTITY_DATA);
        if (entity != null && customData != null && customData.isPresent()) {
            entity.load(customData.get().copyTag());
        }
        return entity;
    }
}
