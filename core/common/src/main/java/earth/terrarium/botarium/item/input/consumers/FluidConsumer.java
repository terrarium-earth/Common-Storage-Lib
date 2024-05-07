package earth.terrarium.botarium.item.input.consumers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.fluid.FluidApi;
import earth.terrarium.botarium.item.input.ConsumerType;
import earth.terrarium.botarium.item.input.ItemConsumer;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.ingredient.SizedFluidIngredient;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public record FluidConsumer(SizedFluidIngredient ingredient, Component name) implements ItemConsumer {
    public static final MapCodec<FluidConsumer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SizedFluidIngredient.FLAT_MB_CODEC.forGetter(FluidConsumer::ingredient),
            ComponentSerialization.CODEC.fieldOf("name").forGetter(FluidConsumer::name)
    ).apply(instance, FluidConsumer::new));
    public static final ConsumerType<FluidConsumer> TYPE = new ConsumerType<>(new ResourceLocation(Botarium.MOD_ID, "fluid"), CODEC);

    @Override
    public boolean test(ItemStack stack, ItemContext context) {
        CommonStorage<FluidResource> storage = FluidApi.ITEM.find(stack, context);
        return storage != null && TransferUtil.extractFluid(storage, ingredient, true).amount() > ingredient.getAmount();
    }

    @Override
    public void consume(ItemStack stack, ItemContext context) {
        CommonStorage<FluidResource> storage = FluidApi.ITEM.find(stack, context);
        TransferUtil.extractFluid(storage, ingredient, false);
    }

    @Override
    public ItemStack modifyDisplay(ItemStack stack) {
        ItemLore lore = stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY);
        lore = lore.withLineAdded(Component.translatable("misc.botarium.consume", Component.translatable("misc.botarium.fluid", Component.literal(String.valueOf(ingredient.getAmountAsMb())), name).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        stack.set(DataComponents.LORE, lore);
        return stack;
    }

    @Override
    public ConsumerType<?> getType() {
        return null;
    }
}
