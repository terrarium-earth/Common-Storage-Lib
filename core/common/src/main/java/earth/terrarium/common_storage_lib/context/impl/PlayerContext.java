package earth.terrarium.common_storage_lib.context.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.impl.vanilla.PlayerContainer;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record PlayerContext(PlayerContainer outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static PlayerContext ofHand(Player player, InteractionHand hand) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.getHandSlot(hand));
    }

    public static PlayerContext ofSlot(Player player, int slot) {
        ItemStack stack = player.getInventory().getItem(slot);
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.get(slot));
    }
}
