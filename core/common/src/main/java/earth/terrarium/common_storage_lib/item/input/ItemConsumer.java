package earth.terrarium.common_storage_lib.item.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.context.ItemContext;
import net.minecraft.world.item.ItemStack;

public interface ItemConsumer {
    Codec<ItemConsumer> CODEC = ItemConsumerRegistry.TYPE_CODEC.dispatch(ItemConsumer::getType, type -> type.codec().codec());
    MapCodec<ItemConsumer> MAP_CODEC = ItemConsumerRegistry.TYPE_CODEC.dispatchMap(ItemConsumer::getType, type -> type.codec().codec());
    ByteCodec<ItemConsumer> BYTE_CODEC = ItemConsumerRegistry.BYTE_CODEC.dispatch(type -> (ByteCodec<ItemConsumer>) type.byteCodec(), ItemConsumer::getType);

    boolean test(ItemStack stack, ItemContext context);

    void consume(ItemStack stack, ItemContext context);

    ItemStack modifyDisplay(ItemStack stream);

    ConsumerType<?> getType();
}
