package earth.terrarium.common_storage_lib.item.input.consumers;

import com.mojang.serialization.MapCodec;
import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.item.input.ConsumerType;
import earth.terrarium.common_storage_lib.item.input.ItemConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CompoundConsumer(List<ItemConsumer> consumers) implements ItemConsumer {
    public static final MapCodec<CompoundConsumer> CODEC = ItemConsumer.MAP_CODEC.codec().listOf().fieldOf("children").xmap(CompoundConsumer::new, CompoundConsumer::consumers);
    public static final ConsumerType<CompoundConsumer> TYPE = new ConsumerType<>(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "compound"), CODEC);

    @Override
    public boolean test(ItemStack stack, ItemContext context) {
        return consumers.stream().allMatch(consumer -> consumer.test(stack, context));
    }

    @Override
    public void consume(ItemStack stack, ItemContext context) {
        consumers.forEach(consumer -> consumer.consume(stack, context));
    }

    @Override
    public ItemStack modifyDisplay(ItemStack stream) {
        return consumers.stream().reduce(stream, (stack, consumer) -> consumer.modifyDisplay(stack), (stack1, stack2) -> stack1);
    }

    @Override
    public ConsumerType<?> getType() {
        return TYPE;
    }
}
