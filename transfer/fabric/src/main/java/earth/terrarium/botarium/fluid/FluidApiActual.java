package earth.terrarium.botarium.fluid;

import earth.terrarium.botarium.BotariumStorage;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.wrapped.WrappedBlockLookup;
import earth.terrarium.botarium.wrapped.WrappedItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofFluid();
    @Actual
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM = new WrappedItemLookup.OfFluid();
    @Actual
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = EntityLookup.createAutomation(new ResourceLocation(BotariumStorage.MOD_ID, "entity_fluid"), CommonStorage.asClass());
}
