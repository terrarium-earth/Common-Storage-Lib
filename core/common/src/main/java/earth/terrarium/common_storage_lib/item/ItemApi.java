package earth.terrarium.common_storage_lib.item;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("NoMatchingActual")
@Expect
public class ItemApi {
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY;
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION;
}
