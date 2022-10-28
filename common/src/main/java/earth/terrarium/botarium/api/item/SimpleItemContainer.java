package earth.terrarium.botarium.api.item;

import earth.terrarium.botarium.api.Updatable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public class SimpleItemContainer implements SerializableContainer {

    private final NonNullList<ItemStack> items;
    private final Updatable updatable;
    private final Predicate<Player> canPlayerAccess;

    public SimpleItemContainer(Updatable entity, int size, Predicate<Player> canPlayerAccess) {
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.updatable = entity;
        this.canPlayerAccess = canPlayerAccess;
    }

    public <T extends BlockEntity & Updatable> SimpleItemContainer(T entity, int size) {
        this(entity, size, player -> entity.getBlockPos().distSqr(player.blockPosition()) <= 64);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return ContainerHelper.saveAllItems(nbt, items);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(items, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(items, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        items.set(i, itemStack);
    }

    @Override
    public void setChanged() {
        updatable.update();
    }

    @Override
    public boolean stillValid(Player player) {
        return canPlayerAccess.test(player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
