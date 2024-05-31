package earth.terrarium.botarium.resources.fluid.ingredient;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.botarium.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

public record FluidIngredientType<T extends FluidIngredient>(ResourceLocation id, MapCodec<T> codec, ByteCodec<T> streamCodec) {
    public FluidIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteCodecUtils.fromCodec(codec.codec()));
    }
}
