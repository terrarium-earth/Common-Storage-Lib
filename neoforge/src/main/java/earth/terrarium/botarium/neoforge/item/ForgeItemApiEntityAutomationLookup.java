package earth.terrarium.botarium.neoforge.item;

import earth.terrarium.botarium.common.generic.base.EntityContainerLookup;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.neoforge.generic.NeoForgeEntityContainerLookup;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ForgeItemApiEntityAutomationLookup implements EntityContainerLookup<ItemContainer, Direction> {
    public static final ForgeItemApiEntityAutomationLookup INSTANCE = new ForgeItemApiEntityAutomationLookup();

    private final NeoForgeEntityContainerLookup<IItemHandler, Direction> lookup = new NeoForgeEntityContainerLookup<>(Capabilities.ItemHandler.ENTITY_AUTOMATION);

    @Override
    public ItemContainer find(Entity entity, @Nullable Direction context) {
        return PlatformItemContainer.of(lookup.find(entity, context));
    }

    @Override
    public void registerEntityTypes(EntityGetter<ItemContainer, @Nullable Direction> getter, Supplier<EntityType<?>>... containers) {
        lookup.registerEntityTypes((entity, context) -> ForgeItemContainer.of(getter.getContainer(entity, context)), containers);
    }
}
