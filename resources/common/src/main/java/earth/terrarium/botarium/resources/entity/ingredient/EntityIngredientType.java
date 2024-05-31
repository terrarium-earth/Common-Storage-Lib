package earth.terrarium.botarium.resources.entity.ingredient;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.botarium.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

public record EntityIngredientType<T extends EntityIngredient>(ResourceLocation id, MapCodec<T> codec, ByteCodec<T> byteCodec) {
    public EntityIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteCodecUtils.fromCodec(codec.codec()));
    }
}
