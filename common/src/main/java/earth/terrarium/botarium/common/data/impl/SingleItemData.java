package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.impl.SimpleSlot;
import earth.terrarium.botarium.common.storage.impl.SingleUnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public record SingleItemData(ItemUnit item, long amount) {
    public static final SingleItemData EMPTY = new SingleItemData(ItemUnit.BLANK, 0);

    public static final Supplier<SingleItemData> DEFAULT = () -> EMPTY;

    public static final Codec<SingleItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(data -> data.item.unit()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(data -> data.item.components()),
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
            buf.writeVarInt(BuiltInRegistries.ITEM.getId(data.item.unit()));
            DataComponentPatch.STREAM_CODEC.encode(buf, data.item.components());
            buf.writeLong(data.amount);
        }
    };

    public static final ContainerSerializer<SimpleSlot<ItemUnit, ?>, SingleItemData> SERIALIZER = new ContainerSerializer<>() {
        @Override
        public SingleItemData captureData(SimpleSlot<ItemUnit, ?> container) {
            return new SingleItemData(container.getUnit(), container.getAmount());
        }

        @Override
        public void applyData(SimpleSlot<ItemUnit, ?> container, SingleItemData data) {
            container.setUnit(data.item);
            container.setAmount(data.amount);
        }
    };

    private static SingleItemData of(Item item, DataComponentPatch components, long amount) {
        return new SingleItemData(ItemUnit.of(item, components), amount);
    }
}
