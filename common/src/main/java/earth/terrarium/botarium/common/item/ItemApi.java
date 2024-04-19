package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class ItemApi {
    public static final BlockLookup<UnitContainer<ItemUnit>, @Nullable Direction> BLOCK;
    public static final ItemLookup<UnitContainer<ItemUnit>, ItemContext> ITEM;
    public static final EntityLookup<UnitContainer<ItemUnit>, Void> ENTITY;
}
