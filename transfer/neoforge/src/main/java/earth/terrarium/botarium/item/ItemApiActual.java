package earth.terrarium.botarium.item;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.item.lookup.ItemBlockLookup;
import earth.terrarium.botarium.item.lookup.ItemEntityLookup;
import earth.terrarium.botarium.item.lookup.ItemItemLookup;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK = ItemBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM = ItemItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY = ItemEntityLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION = ItemEntityLookup.AUTOMATION;
}
