package earth.terrarium.common_storage_lib.item;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.lookup.ItemBlockLookup;
import earth.terrarium.common_storage_lib.item.lookup.ItemEntityLookup;
import earth.terrarium.common_storage_lib.item.lookup.ItemItemLookup;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
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
