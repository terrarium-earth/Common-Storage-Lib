package earth.terrarium.botarium.resources.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.ResourceComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public final class ItemResource extends ResourceComponent implements Predicate<ItemResource>, ItemLike {
    public static final ItemResource BLANK = ItemResource.of(Items.AIR, null);

    public static final Codec<ItemResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemResource::getItem),
            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(ItemResource::getTag)
    ).apply(instance, ItemResource::of));

    public static final MapCodec<ItemResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemResource::getItem),
            CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(ItemResource::getTag)
    ).apply(instance, ItemResource::of));

    public static ItemResource of(ItemLike item) {
        return new ItemResource(item.asItem(), null);
    }

    public static ItemResource of(Holder<Item> holder) {
        return of(holder.value());
    }

    public static ItemResource of(ItemLike item, CompoundTag tag) {
        return new ItemResource(item.asItem(), tag);
    }

    public static ItemResource of(Holder<Item> holder, CompoundTag tag) {
        return new ItemResource(holder.value(), tag);
    }

    public static ItemResource of(ItemStack stack) {
        return of(stack.getItem(), stack.getTag());
    }

    private final Item type;
    private ItemStack cachedStack;

    public ItemResource(Item type, CompoundTag tag) {
        super(tag);
        this.type = type;
    }

    public Item getItem() {
        return type;
    }

    @Override
    public boolean isBlank() {
        return type == Items.AIR;
    }

    public boolean test(ItemStack stack) {
        return isOf(stack.getItem()) && tagsMatch(this.getTag());
    }

    public boolean isOf(Item item) {
        return type == item;
    }

    public ItemStack toStack(int count) {
        ItemStack stack = new ItemStack(type, count);
        stack.setTag(getTag());
        return stack;
    }

    public ItemStack toStack() {
        return toStack(1);
    }

    public ItemStack getCachedStack() {
        ItemStack stack = cachedStack;
        if (stack == null) {
            cachedStack = stack = toStack();
        }
        return stack;
    }

    public <T> ItemResource set(Codec<T> codec, String key, T value) {
        CompoundTag tag = getTag();

        if (tag == null) {
            tag = new CompoundTag();
        } else {
            tag = tag.copy();
        }

        tag.put(key, codec.encodeStart(NbtOps.INSTANCE, value).result().orElseThrow());
        return new ItemResource(type, tag);
    }

    public ItemResource remove(String key) {
        CompoundTag tag = getTag();
        if (tag == null) return this;
        tag = tag.copy();
        tag.remove(key);
        return new ItemResource(type, tag);
    }

    public boolean is(TagKey<Item> tag) {
        return type.builtInRegistryHolder().is(tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemResource) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, tag);
    }

    @Override
    public String toString() {
        return "ItemResource[" +
                "type=" + type + ", " +
                "tag=" + tag + ']';
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

    public boolean hasCraftingRemainder() {
        return ItemResource.hasCraftingRemainder(this);
    }

    @Expect
    private static ItemResource getCraftingRemainder(ItemResource resource);

    @Expect
    private static boolean hasCraftingRemainder(ItemResource resource);

    @Override
    public boolean test(ItemResource resource) {
        return false;
    }
}
