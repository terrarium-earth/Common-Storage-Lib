package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.*;
import earth.terrarium.botarium.common.lookup.wrapped.WrappedBlockLookup;
import earth.terrarium.botarium.common.storage.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<UnitContainer<ItemUnit>, @Nullable Direction> BLOCK = new WrappedBlockLookup<>(ItemStorage.SIDED, ConversionUtils::toVariant, ConversionUtils::toUnit);
    @Actual
    public static final ItemLookup<UnitContainer<ItemUnit>, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(Botarium.MOD_ID, "item_item"), UnitContainer.asClass(), ItemContext.class);
    @Actual
    public static final EntityLookup<UnitContainer<ItemUnit>, Void> ENTITY = EntityLookup.create(new ResourceLocation(Botarium.MOD_ID, "entity_item"), UnitContainer.asClass(), Void.class);
}
