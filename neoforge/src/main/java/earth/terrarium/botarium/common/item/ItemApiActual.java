package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.lookup.ItemBlockLookup;
import earth.terrarium.botarium.common.item.lookup.ItemEntityLookup;
import earth.terrarium.botarium.common.item.lookup.ItemItemLookup;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<UnitContainer<ItemUnit>, @Nullable Direction> BLOCK = new ItemBlockLookup();
    @Actual
    public static final ItemLookup<UnitContainer<ItemUnit>, ItemContext> ITEM = new ItemItemLookup();
    @Actual
    public static final EntityLookup<UnitContainer<ItemUnit>, Void> ENTITY = new ItemEntityLookup<>(Capabilities.ItemHandler.ENTITY);
    @Actual
    public static final EntityLookup<UnitContainer<ItemUnit>, Direction> ENTITY_AUTOMATION = new ItemEntityLookup<>(Capabilities.ItemHandler.ENTITY_AUTOMATION);
}
