package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.impl.SimpleContainer;
import earth.terrarium.botarium.common.storage.impl.SimpleSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ItemContainerData(List<SingleItemData> stacks) {
    public static final Codec<ItemContainerData> CODEC = SingleItemData.CODEC.listOf().xmap(ItemContainerData::new, ItemContainerData::stacks);

    public static final ItemContainerData EMPTY = new ItemContainerData(List.of());

    public static final Supplier<ItemContainerData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<SingleItemData>) new ArrayList<SingleItemData>(), SingleItemData.STREAM_CODEC).map(ItemContainerData::new, ItemContainerData::stacks);

    public static final ContainerSerializer<SimpleContainer<ItemUnit, ?, ?>, ItemContainerData> SERIALIZER = new ContainerSerializer<>() {
        @Override
        public ItemContainerData captureData(SimpleContainer<ItemUnit, ?, ?> buf) {
            List<SingleItemData> stacks = NonNullList.withSize(buf.getSlotCount(), SingleItemData.EMPTY);
            for (int i = 0; i < buf.getSlotCount(); i++) {
                UnitSlot<ItemUnit> slot = buf.getSlot(i);
                stacks.set(i, new SingleItemData(slot.getUnit(), slot.getAmount()));
            }
            return new ItemContainerData(stacks);
        }

        @Override
        public void applyData(SimpleContainer<ItemUnit, ?, ?> object, ItemContainerData data) {
            for (int i = 0; i < Math.min(object.getSlotCount(), data.stacks.size()); i++) {
                SimpleSlot<ItemUnit, ?> slot = object.getSlots().get(i);
                SingleItemData stack = data.stacks.get(i);
                slot.setUnit(stack.item());
                slot.setAmount(stack.amount());
            }
        }
    };
}
