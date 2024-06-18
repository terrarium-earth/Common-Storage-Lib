package earth.terrarium.common_storage_lib.item;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.wrapped.WrappedBlockLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class ItemApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<ItemResource>, @Nullable Direction> BLOCK = new WrappedBlockLookup.ofItem();
    @Actual
    public static final ItemLookup<CommonStorage<ItemResource>, ItemContext> ITEM = ItemLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Void> ENTITY = EntityLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "entity_item"), CommonStorage.asClass(), Void.class);
    @Actual
    public static final EntityLookup<CommonStorage<ItemResource>, Direction> ENTITY_AUTOMATION = EntityLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "entity_item_automation"), CommonStorage.asClass(), Direction.class);
}
