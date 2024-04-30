package earth.terrarium.botarium.item;

import earth.terrarium.botarium.BotariumTransfer;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.wrapped.WrappedBlockLookup;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemUnit>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofItem();
    @Actual
    public static final ItemLookup<CommonStorage<ItemUnit>, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(BotariumTransfer.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemUnit>, Void> ENTITY = EntityLookup.create(new ResourceLocation(BotariumTransfer.MOD_ID, "entity_item"), CommonStorage.asClass(), Void.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemUnit>, Direction> ENTITY_AUTOMATION = EntityLookup.create(new ResourceLocation(BotariumTransfer.MOD_ID, "entity_item_automation"), CommonStorage.asClass(), Direction.class);
}
