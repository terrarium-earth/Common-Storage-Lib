package earth.terrarium.botarium.storage.util;

import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.item.ItemStack;

public interface ModifiableItemSlot {
    void setAmount(long amount);

    void setResource(ItemResource resource);

    default void set(ItemStack stack) {
        setResource(ItemResource.of(stack));
        setAmount(stack.getCount());
        UpdateManager.batch(this);
    }

    default void set(ResourceStack<ItemResource> stack) {
        setResource(stack.resource());
        setAmount(stack.amount());
    }

    ItemStack toItemStack();

    int getMaxAllowed(ItemResource resource);

    boolean isEmpty();
}
