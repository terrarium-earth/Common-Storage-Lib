package earth.terrarium.botarium.item;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class ItemApi {
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY;
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION;
}
