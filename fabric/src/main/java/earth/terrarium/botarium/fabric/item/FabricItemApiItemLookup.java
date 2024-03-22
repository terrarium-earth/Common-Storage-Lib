package earth.terrarium.botarium.fabric.item;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.fabric.generic.FabricItemContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FabricItemApiItemLookup extends FabricItemContainerLookup<ItemContainer, Void> {
    public static final FabricItemApiItemLookup INSTANCE = new FabricItemApiItemLookup();

    public FabricItemApiItemLookup() {
        super(new ResourceLocation(Botarium.MOD_ID, "item"), ItemContainer.class, Void.class);
    }

    @Override
    public void registerItems(ItemGetter<ItemContainer, Void> getter, Supplier<Item>... containers) {
        super.registerItems((stack, context) -> UpdatingItemContainer.of(getter.getContainer(stack, context)), containers);
    }
}
