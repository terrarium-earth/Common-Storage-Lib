package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.impl.vanilla.PlayerContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;

public record PlayerContext(PlayerContainer outerContainer, UnitSlot<ItemUnit> mainSlot) implements ItemContext {
    public static PlayerContext ofHand(Inventory inventory, InteractionHand hand) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(inventory);
        return new PlayerContext(playerContainer, playerContainer.getHandSlot(hand));
    }

    public static PlayerContext ofSlot(Inventory inventory, int slot) {
        PlayerContainer playerContainer = new PlayerContainer.AutoDrop(inventory);
        return new PlayerContext(playerContainer, playerContainer.getSlot(slot));
    }
}
