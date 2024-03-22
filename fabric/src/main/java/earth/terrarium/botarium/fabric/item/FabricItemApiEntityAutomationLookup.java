package earth.terrarium.botarium.fabric.item;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.fabric.generic.FabricEntityContainerLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class FabricItemApiEntityAutomationLookup extends FabricEntityContainerLookup<ItemContainer, Direction> {
    public static final FabricItemApiEntityAutomationLookup INSTANCE = new FabricItemApiEntityAutomationLookup();

    public FabricItemApiEntityAutomationLookup() {
        super(new ResourceLocation(Botarium.MOD_ID, "entity_automation"), ItemContainer.class, Direction.class);
    }

    @Override
    public void registerEntityTypes(EntityGetter<ItemContainer, Direction> getter, Supplier<EntityType<?>>... containers) {
        super.registerEntityTypes((entity, context) -> UpdatingItemContainer.of(getter.getContainer(entity, context)), containers);
    }
}
