package earth.terrarium.botarium.item.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import earth.terrarium.botarium.item.input.consumers.CompoundConsumer;
import earth.terrarium.botarium.item.input.consumers.EnergyConsumer;
import earth.terrarium.botarium.item.input.consumers.FluidConsumer;
import earth.terrarium.botarium.item.input.consumers.SizedConsumer;
import earth.terrarium.botarium.resources.fluid.ingredient.FluidIngredientType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemConsumerRegistry {
    public static final Map<ResourceLocation, ConsumerType<?>> CONSUMER_TYPES = new HashMap<>();
    public static final Codec<ConsumerType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(ItemConsumerRegistry::decode, ConsumerType::id);
    public static final StreamCodec<ByteBuf, ConsumerType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(CONSUMER_TYPES::get, ConsumerType::id);

    public static void register(ConsumerType<?> type) {
        CONSUMER_TYPES.put(type.id(), type);
    }

    static {
        register(SizedConsumer.TYPE);
        register(CompoundConsumer.TYPE);
        register(EnergyConsumer.TYPE);
        register(FluidConsumer.TYPE);
    }

    private static DataResult<? extends ConsumerType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(CONSUMER_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
