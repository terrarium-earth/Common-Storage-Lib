package earth.terrarium.common_storage_lib.item.input.consumers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.item.input.ConsumerType;
import earth.terrarium.common_storage_lib.item.input.ItemConsumer;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record SizedConsumer(long amount) implements ItemConsumer {
    public static final SizedConsumer DEFAULT = new SizedConsumer(1);
    public static final Codec<SizedConsumer> CODEC = Codec.LONG.xmap(SizedConsumer::new, SizedConsumer::amount);
    public static final MapCodec<SizedConsumer> MAP_CODEC = CODEC.optionalFieldOf("count", DEFAULT);
    public static final ConsumerType<SizedConsumer> TYPE = new ConsumerType<>(new ResourceLocation(CommonStorageLib.MOD_ID, "sized"), MAP_CODEC);

    @Override
    public boolean test(ItemStack stack, ItemContext context) {
        if (stack.getCount() < amount) return false;
        if (context.getResource().hasCraftingRemainder()) {
            ItemResource remaining = context.getResource().getCraftingRemainder();
            long exchanged = context.exchange(remaining, amount, true);
            return exchanged == amount;
        } else {
            return true;
        }
    }

    @Override
    public void consume(ItemStack stack, ItemContext context) {
        if (context.getResource().hasCraftingRemainder()) {
            ItemResource remaining = context.getResource().getCraftingRemainder();
            context.exchange(remaining, stack.getCount(), false);
        } else {
            context.extract(context.getResource(), amount, false);
        }
    }

    @Override
    public ItemStack modifyDisplay(ItemStack stack) {
        return stack.copyWithCount((int) amount);
    }

    @Override
    public ConsumerType<?> getType() {
        return TYPE;
    }
}
