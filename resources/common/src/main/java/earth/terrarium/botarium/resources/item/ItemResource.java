package earth.terrarium.botarium.resources.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.TransferResource;
import earth.terrarium.botarium.resources.ResourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public final class ItemResource extends TransferResource<Item, ItemResource> implements Predicate<ItemResource>, ItemLike {
    public static final ItemResource BLANK = ItemResource.of(Items.AIR, DataComponentPatch.EMPTY);

    public static final Codec<ItemResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemResource::getDataPatch)
    ).apply(instance, ItemResource::of));

    public static final MapCodec<ItemResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemResource::getDataPatch)
    ).apply(instance, ItemResource::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemResource> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM),
            ItemResource::asHolder,
            DataComponentPatch.STREAM_CODEC,
            ItemResource::getDataPatch,
            ItemResource::of
    );

    public static ItemResource of(ItemLike item) {
        return new ItemResource(item.asItem(), new PatchedDataComponentMap(item.asItem().components()));
    }

    public static ItemResource of(Holder<Item> holder) {
        return of(holder.value());
    }

    public static ItemResource of(ItemLike item, DataComponentPatch components) {
        return new ItemResource(item.asItem(), PatchedDataComponentMap.fromPatch(item.asItem().components(), components));
    }

    public static ItemResource of(Holder<Item> holder, DataComponentPatch components) {
        return new ItemResource(holder.value(), PatchedDataComponentMap.fromPatch(holder.value().components(), components));
    }

    public static ItemResource of(ItemStack stack) {
        return of(stack.getItem(), stack.getComponentsPatch());
    }

    ItemStack cachedStack;

    public ItemResource(Item type, PatchedDataComponentMap components) {
        super(type, components);
    }

    @Override
    public boolean isBlank() {
        return type == Items.AIR;
    }

    public boolean test(ItemStack stack) {
        return isOf(stack.getItem()) && componentsMatch(stack.getComponentsPatch());
    }

    public ItemStack toItemStack(int count) {
        ItemStack stack = new ItemStack(type, count);
        stack.applyComponents(components);
        return stack;
    }

    public ItemStack toItemStack() {
        return toItemStack(1);
    }

    public ItemStack getCachedStack() {
        ItemStack stack = cachedStack;
        if (stack == null) {
            cachedStack = stack = toItemStack();
        }
        return stack;
    }

    public boolean is(TagKey<Item> tag) {
        return type.builtInRegistryHolder().is(tag);
    }

    @Override
    public <D> ItemResource set(DataComponentType<D> type, D value) {
        PatchedDataComponentMap copy = components.copy();
        copy.set(type, value);
        return new ItemResource(this.type, copy);
    }

    @Override
    public ItemResource modify(DataComponentPatch patch) {
        PatchedDataComponentMap copy = components.copy();
        copy.applyPatch(patch);
        return new ItemResource(this.type, copy);
    }

    @Override
    public ResourceStack<ItemResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemResource) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, components);
    }

    @Override
    public String toString() {
        return "ItemResource[" +
                "type=" + type + ", " +
                "components=" + components + ']';
    }

    @Override
    public @NotNull Item asItem() {
        return type;
    }

    public Holder<Item> asHolder() {
        return type.builtInRegistryHolder();
    }

    public ItemResource getCraftingRemainder() {
        return ItemResource.getCraftingRemainder(this);
    }

    public boolean hasCraftingRemainingItem() {
        return ItemResource.hasCraftingRemainingItem(this);
    }

    @Expect
    private static ItemResource getCraftingRemainder(ItemResource resource);

    @Expect
    private static boolean hasCraftingRemainingItem(ItemResource resource);
}
