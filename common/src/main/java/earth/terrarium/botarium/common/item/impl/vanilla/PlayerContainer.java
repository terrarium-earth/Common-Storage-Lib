package earth.terrarium.botarium.common.item.impl.vanilla;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerContainer extends AbstractVanillaContainer implements UpdateManager<PlayerContainer.InventoryState> {
    private final Inventory inventory;
    private final List<ItemStack> toDrop = new ArrayList<>();

    public PlayerContainer(Inventory container) {
        super(container);
        this.inventory = container;
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        long leftover = amount - super.insert(unit, amount, simulate);
        if (leftover > 0 && !simulate) {
            ItemStack fakeStack = unit.toStack();
            // add as many item stacks as needed to toDrop that are each less than or equal to the max stack size, but totalling the leftover amount
            while (leftover > 0) {
                int stackSize = (int) Math.min(leftover, fakeStack.getMaxStackSize());
                leftover -= stackSize;
                toDrop.add(unit.toStack(stackSize));
            }
        }
        return amount;
    }

    @Override
    public InventoryState createSnapshot() {
        NonNullList<ItemStack> items = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            items.set(i, inventory.getItem(i).copy());
        }
        return new InventoryState(items, toDrop.size());
    }

    @Override
    public void readSnapshot(InventoryState snapshot) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, snapshot.items.get(i));
        }
        if (toDrop.size() > snapshot.droppedItemCount) {
            List<ItemStack> itemStacks = toDrop.subList(0, snapshot.droppedItemCount);
            toDrop.clear();
            toDrop.addAll(itemStacks);
        }
    }

    @Override
    public void update() {
        for (ItemStack stack : toDrop) {
            inventory.player.drop(stack, false);
        }
        toDrop.clear();
    }

    public record InventoryState(NonNullList<ItemStack> items, int droppedItemCount) {}
}
