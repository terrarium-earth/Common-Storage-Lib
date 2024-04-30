package earth.terrarium.botarium.resource;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resource.fluid.FluidResource;
import earth.terrarium.botarium.resource.item.ItemResource;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ResourceStack<T extends TransferResource<?, T>>(T unit, long amount) implements DataComponentHolder {
    public static Codec<ResourceStack<FluidResource>> FLUID_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidResource.MAP_CODEC.forGetter(ResourceStack::unit),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static Codec<ResourceStack<ItemResource>> ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemResource.CODEC.fieldOf("unit").forGetter(ResourceStack::unit),
            Codec.LONG.fieldOf("amount").forGetter(ResourceStack::amount)
    ).apply(instance, ResourceStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<FluidResource>> FLUID_STREAM_CODEC = StreamCodec.composite(FluidResource.STREAM_CODEC, ResourceStack::unit, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceStack<ItemResource>> ITEM_STREAM_CODEC = StreamCodec.composite(ItemResource.STREAM_CODEC, ResourceStack::unit, ByteBufCodecs.VAR_LONG, ResourceStack::amount, ResourceStack::new);

    public static final ResourceStack<FluidResource> EMPTY_FLUID = new ResourceStack<>(FluidResource.BLANK, 0);
    public static final ResourceStack<ItemResource> EMPTY_ITEM = new ResourceStack<>(ItemResource.BLANK, 0);

    public static ItemStack toItemStack(ResourceStack<ItemResource> stack) {
        return stack.unit().toItemStack((int) stack.amount());
    }

    public ResourceStack<T> grow(long amount) {
        return new ResourceStack<>(unit, this.amount + amount);
    }

    public ResourceStack<T> shrink(long amount) {
        return new ResourceStack<>(unit, this.amount - amount);
    }

    public ResourceStack<T> withCount(long amount) {
        return new ResourceStack<>(unit, amount);
    }

    public <D> ResourceStack<T> set(DataComponentType<D> type, D data) {
        return new ResourceStack<>(unit.set(type, data), amount);
    }

    public boolean isEmpty() {
        return amount <= 0 || unit.isBlank();
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return unit.getComponents();
    }

    @Override
    public long amount() {
        return isEmpty() ? 0 : amount;
    }
}
