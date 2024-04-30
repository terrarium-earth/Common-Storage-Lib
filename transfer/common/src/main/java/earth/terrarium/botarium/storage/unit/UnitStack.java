package earth.terrarium.botarium.storage.unit;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record UnitStack<T extends TransferUnit<?, T>>(T unit, long amount) implements DataComponentHolder {
    public static Codec<UnitStack<FluidUnit>> FLUID_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidUnit.MAP_CODEC.forGetter(UnitStack::unit),
            Codec.LONG.fieldOf("amount").forGetter(UnitStack::amount)
    ).apply(instance, UnitStack::new));

    public static Codec<UnitStack<ItemUnit>> ITEM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemUnit.CODEC.fieldOf("unit").forGetter(UnitStack::unit),
            Codec.LONG.fieldOf("amount").forGetter(UnitStack::amount)
    ).apply(instance, UnitStack::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, UnitStack<FluidUnit>> FLUID_STREAM_CODEC = StreamCodec.composite(FluidUnit.STREAM_CODEC, UnitStack::unit, ByteBufCodecs.VAR_LONG, UnitStack::amount, UnitStack::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, UnitStack<ItemUnit>> ITEM_STREAM_CODEC = StreamCodec.composite(ItemUnit.STREAM_CODEC, UnitStack::unit, ByteBufCodecs.VAR_LONG, UnitStack::amount, UnitStack::new);

    public static final UnitStack<FluidUnit> EMPTY_FLUID = new UnitStack<>(FluidUnit.BLANK, 0);
    public static final UnitStack<ItemUnit> EMPTY_ITEM = new UnitStack<>(ItemUnit.BLANK, 0);

    public static <T extends TransferUnit<?, T>> UnitStack<T> from(StorageSlot<T> slot) {
        return new UnitStack<>(slot.getUnit(), slot.getAmount());
    }

    public static ItemStack toItemStack(UnitStack<ItemUnit> stack) {
        return stack.unit().toItemStack((int) stack.amount());
    }

    public UnitStack<T> grow(long amount) {
        return new UnitStack<>(unit, this.amount + amount);
    }

    public UnitStack<T> shrink(long amount) {
        return new UnitStack<>(unit, this.amount - amount);
    }

    public UnitStack<T> withCount(long amount) {
        return new UnitStack<>(unit, amount);
    }

    public <D> UnitStack<T> set(DataComponentType<D> type, D data) {
        return new UnitStack<>(unit.set(type, data), amount);
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
