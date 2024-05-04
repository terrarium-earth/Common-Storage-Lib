package earth.terrarium.botarium.item;

import earth.terrarium.botarium.BotariumStorage;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.wrapped.WrappedBlockLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofItem();
    @Actual
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY = EntityLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "entity_item"), CommonStorage.asClass(), Void.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION = EntityLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "entity_item_automation"), CommonStorage.asClass(), Direction.class);
}
