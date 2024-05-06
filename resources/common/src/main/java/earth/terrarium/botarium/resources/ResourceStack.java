package earth.terrarium.botarium.resources;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.util.FluidAmounts;
import earth.terrarium.botarium.resources.item.ItemResource;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ResourceStack<T extends TransferResource<?, T>>(T resource, long amount) implements DataComponentHolder {
    public static Codec<ResourceStack<FluidResource>> FLUID_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static Codec<ResourceStack<FluidResource>> FLUID_MB_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("millibuckets").forGetter(stack -> FluidAmounts.toMillibuckets(stack.amount))
    ).apply(instance, (unit, amount) -> new ResourceStack<>(unit, FluidAmounts.toPlatformAmount(amount))));

    public static Codec<ResourceStack<ItemResource>> ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemResource.MAP_CODEC.forGetter(ResourceStack::resource),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<FluidResource>> FLUID_STREAM_CODEC = StreamCodec.composite(FluidResource.STREAM_CODEC, ResourceStack::resource, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<ItemResource>> ITEM_STREAM_CODEC = StreamCodec.composite(ItemResource.STREAM_CODEC, ResourceStack::resource, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);

    public static final ResourceStack<FluidResource> EMPTY_FLUID = new ResourceStack<>(FluidResource.BLANK, 0);
    public static final ResourceStack<ItemResource> EMPTY_ITEM = new ResourceStack<>(ItemResource.BLANK, 0);

    public static ItemStack toItemStack(ResourceStack<ItemResource> stack) {
        return stack.resource().toItemStack((int) stack.amount());
    }

    public ResourceStack<T> grow(long amount) {
        return new ResourceStack<>(resource, this.amount + amount);
    }

    public ResourceStack<T> shrink(long amount) {
        return new ResourceStack<>(resource, this.amount - amount);
    }

    public ResourceStack<T> withCount(long amount) {
        return new ResourceStack<>(resource, amount);
    }

    public <D> ResourceStack<T> set(DataComponentType<D> type, D data) {
        return new ResourceStack<>(resource.set(type, data), amount);
    }

    public boolean isEmpty() {
        return amount <= 0 || resource.isBlank();
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return resource.getComponents();
    }

    @Override
    public long amount() {
        return isEmpty() ? 0 : amount;
    }
}
