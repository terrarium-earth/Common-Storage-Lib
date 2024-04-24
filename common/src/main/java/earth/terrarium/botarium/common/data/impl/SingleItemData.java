package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.storage.util.SlotExtras;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record SingleItemData(ItemUnit unit, long amount) {
    public static final SingleItemData EMPTY = new SingleItemData(ItemUnit.BLANK, 0);

    public static final Supplier<SingleItemData> DEFAULT = () -> EMPTY;

    public static final Codec<SingleItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(data -> data.unit.type()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(data -> data.unit.components()),
            Codec.LONG.fieldOf("amount").forGetter(SingleItemData::amount)
    ).apply(instance, SingleItemData::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, SingleItemData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SingleItemData decode(RegistryFriendlyByteBuf buf) {
            Item item = BuiltInRegistries.ITEM.byId(buf.readVarInt());
            DataComponentPatch components = DataComponentPatch.STREAM_CODEC.decode(buf);
            long amount = buf.readLong();
            return SingleItemData.of(item, components, amount);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SingleItemData data) {
            buf.writeVarInt(BuiltInRegistries.ITEM.getId(data.unit.type()));
            DataComponentPatch.STREAM_CODEC.encode(buf, data.unit.components());
            buf.writeLong(data.amount);
        }
    };

    private static SingleItemData of(Item item, DataComponentPatch components, long amount) {
        return new SingleItemData(ItemUnit.of(item, components), amount);
    }

    public static SingleItemData of(UnitSlot<ItemUnit> stack) {
        return new SingleItemData(stack.getUnit(), stack.getAmount());
    }

    public ItemStack createStack() {
        return unit.toStack((int) amount);
    }
}
