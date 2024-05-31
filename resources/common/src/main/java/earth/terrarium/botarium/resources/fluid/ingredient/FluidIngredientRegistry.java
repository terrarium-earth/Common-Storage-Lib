package earth.terrarium.botarium.resources.fluid.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.botarium.resources.fluid.ingredient.impl.*;
import earth.terrarium.botarium.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FluidIngredientRegistry {
    public static final Map<ResourceLocation, FluidIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
    public static final Codec<FluidIngredientType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(FluidIngredientRegistry::decode, FluidIngredientType::id);
    public static final ByteCodec<FluidIngredientType<?>> STREAM_CODEC = ByteCodecUtils.RESOURCE_LOCATION.map(INGREDIENT_TYPES::get, FluidIngredientType::id);

    public static void init() {}

    static {
        register(BaseFluidIngredient.TYPE);
        // register(ComponentFluidIngredient.TYPE);
        register(DifferenceFluidIngredient.TYPE);
        register(AllMatchFluidIngredient.TYPE);
        register(AnyMatchFluidIngredient.TYPE);
    }

    public static void register(FluidIngredientType<?> type) {
        INGREDIENT_TYPES.put(type.id(), type);
    }

    private static DataResult<? extends FluidIngredientType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(INGREDIENT_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
