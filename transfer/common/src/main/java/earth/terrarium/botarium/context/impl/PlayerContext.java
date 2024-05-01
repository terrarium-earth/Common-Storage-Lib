package earth.terrarium.botarium.context.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.item.impl.vanilla.PlayerContainer;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public record PlayerContext(PlayerContainer outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static PlayerContext ofHand(Player player, InteractionHand hand) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.getHandSlot(hand));
    }

    public static PlayerContext ofSlot(Player player, int slot) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(player.getInventory());
        return new PlayerContext(playerContainer, playerContainer.getSlot(slot));
    }
}
