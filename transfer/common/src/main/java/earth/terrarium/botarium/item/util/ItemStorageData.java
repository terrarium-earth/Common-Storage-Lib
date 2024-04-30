package earth.terrarium.botarium.item.util;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.unit.UnitStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ItemStorageData(List<UnitStack<ItemUnit>> stacks) {
    public static final Codec<ItemStorageData> CODEC = UnitStack.ITEM_CODEC.listOf().xmap(ItemStorageData::new, ItemStorageData::stacks);

    public static final ItemStorageData EMPTY = new ItemStorageData(List.of());

    public static final Supplier<ItemStorageData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStorageData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<UnitStack<ItemUnit>>) new ArrayList<UnitStack<ItemUnit>>(), UnitStack.ITEM_STREAM_CODEC).map(ItemStorageData::new, ItemStorageData::stacks);

    public static ItemStorageData of(CommonStorage<ItemUnit> container) {
        List<UnitStack<ItemUnit>> stacks = NonNullList.withSize(container.getSlotCount(), UnitStack.EMPTY_ITEM);
        for (int i = 0; i < container.getSlotCount(); i++) {
            stacks.set(i, UnitStack.from(container.getSlot(i)));
        }
        return new ItemStorageData(stacks);
    }
}
