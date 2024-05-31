package earth.terrarium.common_storage_lib.item.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.item.input.consumers.CompoundConsumer;
import earth.terrarium.common_storage_lib.item.input.consumers.EnergyConsumer;
import earth.terrarium.common_storage_lib.item.input.consumers.FluidConsumer;
import earth.terrarium.common_storage_lib.item.input.consumers.SizedConsumer;
import earth.terrarium.common_storage_lib.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemConsumerRegistry {
    public static final Map<ResourceLocation, ConsumerType<?>> CONSUMER_TYPES = new HashMap<>();
    public static final Codec<ConsumerType<?>> TYPE_CODEC = ResourceLocation.CODEC.comapFlatMap(ItemConsumerRegistry::decode, ConsumerType::id);
    public static final ByteCodec<ConsumerType<?>> BYTE_CODEC = ByteCodecUtils.RESOURCE_LOCATION.map(CONSUMER_TYPES::get, ConsumerType::id);

    static {
        register(SizedConsumer.TYPE);
        register(CompoundConsumer.TYPE);
        register(EnergyConsumer.TYPE);
        register(FluidConsumer.TYPE);
    }

    public static void register(ConsumerType<?> type) {
        CONSUMER_TYPES.put(type.id(), type);
    }

    public static void init() {
        // Called from Botarium#init
    }

    private static DataResult<? extends ConsumerType<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(CONSUMER_TYPES.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }
}
