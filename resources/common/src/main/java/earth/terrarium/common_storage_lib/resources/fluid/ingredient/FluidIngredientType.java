package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

public record FluidIngredientType<T extends FluidIngredient>(ResourceLocation id, MapCodec<T> codec, ByteCodec<T> streamCodec) {
    public FluidIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteCodecUtils.fromCodec(codec.codec()));
    }
}
