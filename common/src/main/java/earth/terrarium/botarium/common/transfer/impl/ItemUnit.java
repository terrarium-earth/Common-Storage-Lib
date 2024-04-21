package earth.terrarium.botarium.common.transfer.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Predicate;

public record ItemUnit(Item unit, DataComponentPatch components) implements TransferUnit<Item>, Predicate<ItemUnit> {
    public static final ItemUnit BLANK = new ItemUnit(Items.AIR, DataComponentPatch.EMPTY);

    public static final Codec<ItemUnit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemUnit::unit),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemUnit::components)
    ).apply(instance, ItemUnit::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> ITEM_HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemUnit> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ItemUnit decode(RegistryFriendlyByteBuf object) {
            Holder<Item> holder = ITEM_HOLDER_STREAM_CODEC.decode(object);
            DataComponentPatch dataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(object);
            return new ItemUnit(holder.value(), dataComponentPatch);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf object, ItemUnit object2) {
            ITEM_HOLDER_STREAM_CODEC.encode(object, object2.unit.builtInRegistryHolder());
            DataComponentPatch.STREAM_CODEC.encode(object, object2.components);
        }
    };

    public static ItemUnit of(ItemLike item) {
        return new ItemUnit(item.asItem(), DataComponentPatch.EMPTY);
    }

    public static ItemUnit of(ItemLike item, DataComponentPatch components) {
        return new ItemUnit(item.asItem(), components);
    }

    public static ItemUnit of(ItemStack stack) {
        return new ItemUnit(stack.getItem(), stack.getComponentsPatch());
    }

    @Override
    public boolean isBlank() {
        return unit == Items.AIR;
    }

    public boolean matches(ItemStack stack) {
        return isOf(stack.getItem()) && componentsMatch(stack.getComponentsPatch());
    }

    public ItemStack toStack(int count) {
        ItemStack stack = new ItemStack(unit, count);
        stack.applyComponents(components);
        return stack;
    }

    public ItemStack toStack() {
        return toStack(1);
    }

    @Override
    public boolean test(ItemUnit unit) {
        return isOf(unit.unit) && componentsMatch(unit.components);
    }
}
