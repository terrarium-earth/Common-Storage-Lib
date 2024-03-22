package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import earth.terrarium.botarium.neoforge.item.ForgeItemApiBlockLookup;
import earth.terrarium.botarium.neoforge.item.ForgeItemApiEntityAutomationLookup;
import earth.terrarium.botarium.neoforge.item.ForgeItemApiEntityLookup;
import earth.terrarium.botarium.neoforge.item.ForgeItemApiItemLookup;
import earth.terrarium.botarium.util.Snapshotable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

@ClassExtension(ItemApiImpl.class)
public class ItemApiImpl {

    private static ItemContainerLookup<ItemContainer, Void> getItemLookup() {
        return ForgeItemApiItemLookup.INSTANCE;
    }

    private static BlockContainerLookup<ItemContainer, Direction> getBlockLookup() {
        return ForgeItemApiBlockLookup.INSTANCE;
    }

    private static EntityContainerLookup<ItemContainer, Void> getEntityLookup() {
        return ForgeItemApiEntityLookup.INSTANCE;
    }

    private static EntityContainerLookup<ItemContainer, Direction> getEntityAutomationLookup() {
        return ForgeItemApiEntityAutomationLookup.INSTANCE;
    }
}
