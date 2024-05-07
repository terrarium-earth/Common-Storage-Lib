package earth.terrarium.botarium.item.input.consumers;

import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.input.ConsumerType;
import earth.terrarium.botarium.item.input.ItemConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CompoundConsumer(List<ItemConsumer> consumers) implements ItemConsumer {
    public static final MapCodec<CompoundConsumer> CODEC = ItemConsumer.MAP_CODEC.codec().listOf().fieldOf("children").xmap(CompoundConsumer::new, CompoundConsumer::consumers);
    public static final ConsumerType<CompoundConsumer> TYPE = new ConsumerType<>(new ResourceLocation(Botarium.MOD_ID, "compound"), CODEC);

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
