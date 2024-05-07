package earth.terrarium.botarium.item.input;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.input.consumers.SizedConsumer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemConsumer {
    Codec<ItemConsumer> CODEC = ItemConsumerRegistry.TYPE_CODEC.dispatch(ItemConsumer::getType, ConsumerType::codec);
    MapCodec<ItemConsumer> MAP_CODEC = ItemConsumerRegistry.TYPE_CODEC.dispatchMap(ItemConsumer::getType, ConsumerType::codec);

    @SuppressWarnings("unchecked")
    StreamCodec<RegistryFriendlyByteBuf, ItemConsumer> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf regByteBuf, ItemConsumer ingredient) {
            ItemConsumerRegistry.STREAM_CODEC.encode(regByteBuf, ingredient.getType());
            ((StreamCodec<RegistryFriendlyByteBuf, ItemConsumer>) ingredient.getType().streamCodec()).encode(regByteBuf, ingredient);
        }

        @Override
        public @NotNull ItemConsumer decode(RegistryFriendlyByteBuf regByteBuf) {
            ConsumerType<?> fluidIngredientType = ItemConsumerRegistry.STREAM_CODEC.decode(regByteBuf);
            return fluidIngredientType.streamCodec().decode(regByteBuf);
        }
    };

    boolean test(ItemStack stack, ItemContext context);

    void consume(ItemStack stack, ItemContext context);

    ItemStack modifyDisplay(ItemStack stream);

    ConsumerType<?> getType();
}
