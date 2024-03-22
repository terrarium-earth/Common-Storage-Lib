package earth.terrarium.botarium.fabric.item;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.fabric.generic.FabricEntityContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class FabricItemApiEntityLookup extends FabricEntityContainerLookup<ItemContainer, Void> {
    public static final FabricItemApiEntityLookup INSTANCE = new FabricItemApiEntityLookup();

    public FabricItemApiEntityLookup() {
        super(new ResourceLocation(Botarium.MOD_ID, "entity"), ItemContainer.class, Void.class);
    }

    @Override
    public void registerEntityTypes(EntityGetter<ItemContainer, Void> getter, Supplier<EntityType<?>>... containers) {
        super.registerEntityTypes((entity, context) -> UpdatingItemContainer.of(getter.getContainer(entity, context)), containers);
    }
}
