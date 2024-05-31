package earth.terrarium.botarium.resources.entity.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.botarium.resources.entity.EntityResource;
import earth.terrarium.botarium.resources.entity.ingredient.impl.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public interface EntityIngredient extends Predicate<EntityResource> {
    Codec<EntityIngredient> TYPE_CODEC = EntityIngredientRegistry.TYPE_CODEC.dispatch(EntityIngredient::getType, entityIngredientType -> entityIngredientType.codec().codec());
    Codec<EntityIngredient> CODEC = Codec.either(BaseEntityIngredient.CODEC, TYPE_CODEC).xmap(either -> either.map(l -> l, r -> r), ingredient -> ingredient instanceof BaseEntityIngredient ? Either.left((BaseEntityIngredient) ingredient) : Either.right(ingredient));
    MapCodec<EntityIngredient> MAP_CODEC = EntityIngredientRegistry.TYPE_CODEC.dispatchMap(EntityIngredient::getType, entityIngredientType -> entityIngredientType.codec().codec());

    ByteCodec<EntityIngredient> BYTE_CODEC = EntityIngredientRegistry.BYTE_CODEC.dispatch(type -> (ByteCodec<EntityIngredient>) type.byteCodec(), EntityIngredient::getType);

    static EntityIngredient of(EntityResource... stacks) {
        return BaseEntityIngredient.of(stacks);
    }

    static EntityIngredient of(TagKey<EntityType<?>> tag) {
        return BaseEntityIngredient.of(tag);
    }

    static EntityIngredient all(EntityIngredient... ingredients) {
        return new AllMatchEntityIngredient(Arrays.asList(ingredients));
    }

    static EntityIngredient any(EntityIngredient... ingredients) {
        return new AnyMatchEntityIngredient(Arrays.asList(ingredients));
    }

    static EntityIngredient difference(EntityIngredient minuend, EntityIngredient subtrahend) {
        return new DifferenceEntityIngredient(minuend, subtrahend);
    }

    /*
    static EntityIngredient components(EntityIngredient base, DataComponentPredicate components) {
        return new ComponentEntityIngredient(base, components);
    }
    */

    static SizedEntityIngredient sized(EntityIngredient ingredient, long size) {
        return new SizedEntityIngredient(ingredient, size);
    }

    boolean test(EntityResource EntityResource);

    List<EntityResource> getMatchingEntities();

    boolean requiresTesting();

    EntityIngredientType<?> getType();
}
