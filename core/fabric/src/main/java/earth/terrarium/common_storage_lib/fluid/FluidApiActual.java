package earth.terrarium.common_storage_lib.fluid;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.wrapped.WrappedBlockLookup;
import earth.terrarium.common_storage_lib.wrapped.WrappedItemLookup;
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
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = EntityLookup.createAutomation(new ResourceLocation(CommonStorageLib.MOD_ID, "entity_fluid"), CommonStorage.asClass());
}
