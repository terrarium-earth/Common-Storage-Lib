package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.impl.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FluidIngredientRegistry {
    public static final Map<ResourceLocation, FluidIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();

    public static void init() {}

    static {
        register(BaseFluidIngredient.TYPE);
        register(ComponentFluidIngredient.TYPE);
        register(DifferenceFluidIngredient.TYPE);
        register(AllMatchFluidIngredient.TYPE);
        register(AnyMatchFluidIngredient.TYPE);
    }

    public static void register(FluidIngredientType<?> type) {
        INGREDIENT_TYPES.put(type.id(), type);
    }
}
