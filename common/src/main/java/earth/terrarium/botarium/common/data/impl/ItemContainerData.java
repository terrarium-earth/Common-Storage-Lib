package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.storage.util.ContainerExtras;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
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

    public static ItemContainerData of(UnitContainer<ItemUnit> container) {
        List<SingleItemData> stacks = NonNullList.withSize(container.getSlotCount(), SingleItemData.EMPTY);
        for (int i = 0; i < container.getSlotCount(); i++) {
            UnitSlot<ItemUnit> slot = container.getSlot(i);
            stacks.set(i, new SingleItemData(slot.getUnit(), slot.getAmount()));
        }
        return new ItemContainerData(stacks);
    }
}
