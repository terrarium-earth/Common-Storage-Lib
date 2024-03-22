package earth.terrarium.botarium.neoforge.generic;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.generic.base.ItemContainerLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeItemContainerLookup<T, C> implements ItemContainerLookup<T, C> {
    private final ItemCapability<T, C> capability;
    private final Map<Supplier<Item>, ItemGetter<T, C>> blockEntityGetterMap = new HashMap<>();
    private Map<Item, ItemGetter<T, C>> blockEntityMap = null;

    public NeoForgeItemContainerLookup(ItemCapability<T, C> cap) {
        capability = cap;
    }

    public NeoForgeItemContainerLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        this(ItemCapability.create(name, typeClass, contextClass));
    }

    @Override
    public T find(ItemStack stack, @Nullable C context) {
        return stack.getCapability(capability, context);
    }

    @Override
    public void registerItems(ItemGetter<T, C> getter, Supplier<Item>... containers) {
        for (Supplier<Item> container : containers) {
            blockEntityGetterMap.put(container, getter);
        }
    }

    public void registerWithCaps(RegisterCapabilitiesEvent event) {
        getBlockEntityMap().forEach((blockEntityType, blockGetter) -> event.registerItem(capability, blockGetter::getContainer, blockEntityType));
    }

    public Map<Item, ItemGetter<T, C>> getBlockEntityMap() {
        blockEntityMap = Botarium.finalizeRegistration(blockEntityGetterMap, blockEntityMap);
        return blockEntityMap;
    }

    ItemCapability<T, C> getCapability() {
        return capability;
    }
}
