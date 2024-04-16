package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import net.minecraft.world.item.ItemStack;

public record SimpleItemContext(SlottedContainer<ItemStack> outerContainer, SingleSlotContainer<ItemStack> mainSlot) implements ItemContext {
}
