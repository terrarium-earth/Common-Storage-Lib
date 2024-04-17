package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.impl.PlayerHandContainer;
import earth.terrarium.botarium.common.item.impl.VanillaContainerWrapper;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerItemContext implements ItemContext {
    private final Player player;
    private final SlottedContainer<ItemStack> container;
    private final ContainerSlot<ItemStack> slot;

    public PlayerItemContext(Player player, ContainerSlot<ItemStack> slot) {
        this.player = player;
        this.container = new VanillaContainerWrapper(player.getInventory());
        this.slot = slot;
    }

    public PlayerItemContext(Player player, InteractionHand hand) {
        this(player, new PlayerHandContainer(player, hand));
    }

    @Override
    public long insertIndiscriminately(@NotNull ItemStack value, boolean simulate) {
        if (!simulate) {
            player.getInventory().placeItemBackInInventory(value);
        }
        return value.getCount();
    }

    @Override
    public SlottedContainer<ItemStack> outerContainer() {
        return container;
    }

    @Override
    public ContainerSlot<ItemStack> mainSlot() {
        return slot;
    }
}
