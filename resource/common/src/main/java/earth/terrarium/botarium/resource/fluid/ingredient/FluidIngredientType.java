package earth.terrarium.botarium.resource.fluid.ingredient;

import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.resource.item.ingredient.ItemIngredient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FluidIngredientType<T extends FluidIngredient>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    public FluidIngredientType(MapCodec<T> codec) {
        this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }
}
