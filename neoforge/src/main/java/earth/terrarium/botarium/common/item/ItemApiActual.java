package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<UnitContainer<ItemUnit>, @Nullable Direction> BLOCK;
    @Actual
    public static final ItemLookup<UnitContainer<ItemUnit>, ItemContext> ITEM;
    @Actual
    public static final EntityLookup<UnitContainer<ItemUnit>, Void> ENTITY;
}
