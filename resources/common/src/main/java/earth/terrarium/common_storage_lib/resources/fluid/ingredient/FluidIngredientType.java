package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record FluidIngredientType<T extends FluidIngredient>(ResourceLocation id, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    
    public static final Codec<FluidIngredientType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(FluidIngredientType::decode, FluidIngredientType::id);
    public static final Map<ResourceLocation, FluidIngredientType<?>> INGREDIENT_TYPES = new HashMap<>();
    public static final StreamCodec<ByteBuf, FluidIngredientType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(INGREDIENT_TYPES::get, FluidIngredientType::id);
    
    private static DataResult<? extends FluidIngredientType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(INGREDIENT_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
    
    public FluidIngredientType(ResourceLocation id, MapCodec<T> codec) {
        this(id, codec, ByteBufCodecs.fromCodecWithRegistries(new MapCodec.MapCodecCodec<>(codec)));
    }
}
