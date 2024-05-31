package earth.terrarium.botarium.item.util;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.minecraft.core.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ItemStorageData(List<ResourceStack<ItemResource>> stacks) {
    public static final Codec<ItemStorageData> CODEC = ResourceStack.ITEM_CODEC.listOf().xmap(ItemStorageData::new, ItemStorageData::stacks);
    public static final ItemStorageData EMPTY = new ItemStorageData(List.of());

    public static ItemStorageData of(CommonStorage<ItemResource> container) {
        List<ResourceStack<ItemResource>> stacks = NonNullList.withSize(container.size(), ResourceStack.EMPTY_ITEM);
        for (int i = 0; i < container.size(); i++) {
            StorageSlot<ItemResource> slot = container.get(i);
            stacks.set(i, new ResourceStack<>(slot.getResource(), slot.getAmount()));
        }
        return new ItemStorageData(stacks);
    }
}
