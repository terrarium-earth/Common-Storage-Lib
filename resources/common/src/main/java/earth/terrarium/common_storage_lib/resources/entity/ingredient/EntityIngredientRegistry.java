package earth.terrarium.common_storage_lib.resources.entity.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.resources.entity.ingredient.impl.*;
import earth.terrarium.common_storage_lib.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityIngredientRegistry {
    public static final Map<ResourceLocation, EntityIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
    public static final Codec<EntityIngredientType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(EntityIngredientRegistry::decode, EntityIngredientType::id);
    public static final ByteCodec<EntityIngredientType<?>> BYTE_CODEC = ByteCodecUtils.RESOURCE_LOCATION.map(INGREDIENT_TYPES::get, EntityIngredientType::id);

    public static void init() {}

    static {
        register(BaseEntityIngredient.TYPE);
        // register(ComponentEntityIngredient.TYPE);
        register(DifferenceEntityIngredient.TYPE);
        register(AllMatchEntityIngredient.TYPE);
        register(AnyMatchEntityIngredient.TYPE);
    }

    public static void register(EntityIngredientType<?> type) {
        INGREDIENT_TYPES.put(type.id(), type);
    }

    private static DataResult<? extends EntityIngredientType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(INGREDIENT_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
