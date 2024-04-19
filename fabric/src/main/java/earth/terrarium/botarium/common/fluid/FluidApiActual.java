package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.*;
import earth.terrarium.botarium.common.lookup.wrapped.WrappedBlockLookup;
import earth.terrarium.botarium.common.lookup.wrapped.WrappedItemLookup;
import earth.terrarium.botarium.common.storage.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<UnitContainer<FluidUnit>, @Nullable Direction> BLOCK = new WrappedBlockLookup<>(FluidStorage.SIDED, ConversionUtils::toVariant, ConversionUtils::toUnit);
    @Actual
    public static final ItemLookup<UnitContainer<FluidUnit>, ItemContext> ITEM = new WrappedItemLookup<>(FluidStorage.ITEM, ConversionUtils::toVariant, ConversionUtils::toUnit);
    @Actual
    public static final EntityLookup<UnitContainer<FluidUnit>, Direction> ENTITY = EntityLookup.createAutomation(new ResourceLocation(Botarium.MOD_ID, "entity_fluid"), UnitContainer.asClass());
}
