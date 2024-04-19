package earth.terrarium.botarium.common.transfer.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
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
