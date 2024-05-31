package earth.terrarium.common_storage_lib.fluid;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY;
}
