package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.fabric.item.FabricItemApiBlockLookup;
import earth.terrarium.botarium.fabric.item.FabricItemApiEntityAutomationLookup;
import earth.terrarium.botarium.fabric.item.FabricItemApiEntityLookup;
import earth.terrarium.botarium.fabric.item.FabricItemApiItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.extensions.annotations.ClassExtension;

@ClassExtension(ItemApi.class)
public class ItemApiImpl {
    private static ItemContainerLookup<ItemContainer, Void> getItemLookup() {
        return FabricItemApiItemLookup.INSTANCE;
    }

    private static BlockContainerLookup<ItemContainer, Direction> getBlockLookup() {
        return FabricItemApiBlockLookup.INSTANCE;
    }

    private static EntityContainerLookup<ItemContainer, Void> getEntityLookup() {
        return FabricItemApiEntityLookup.INSTANCE;
    }

    private static EntityContainerLookup<ItemContainer, Direction> getEntityAutomationLookup() {
        return FabricItemApiEntityAutomationLookup.INSTANCE;
    }
}
