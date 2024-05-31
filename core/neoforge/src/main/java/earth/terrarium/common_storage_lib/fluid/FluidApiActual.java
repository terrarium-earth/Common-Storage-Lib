package earth.terrarium.common_storage_lib.fluid;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.fluid.lookup.FluidBlockLookup;
import earth.terrarium.common_storage_lib.fluid.lookup.FluidEntityLookup;
import earth.terrarium.common_storage_lib.fluid.lookup.FluidItemLookup;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK = FluidBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM = FluidItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = FluidEntityLookup.INSTANCE;
}
