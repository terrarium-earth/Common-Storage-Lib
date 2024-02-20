package earth.terrarium.botarium.forge.item;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record SerializedForgeItemContainer<T extends ItemContainer & Updatable<?>>(T container) implements IItemHandler, INBTSerializable<CompoundTag> {
    @Override
    public int getSlots() {
        return container.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        return container.getStackInSlot(i);
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack arg, boolean simulate) {
        ItemStack stack = container.insertIntoSlot(i, arg, simulate);
        if (!simulate) container.update(null);
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int j, boolean simulate) {
        ItemStack stack = container.extractFromSlot(i, j, simulate);
        if (!simulate) container.update(null);
        return stack;
    }

    @Override
    public int getSlotLimit(int i) {
        return container.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack arg) {
        return container.isItemValid(i, arg);
    }

    @Override
    public CompoundTag serializeNBT() {
        if (container instanceof Serializable serializable) {
            return serializable.serialize(new CompoundTag());
        }
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        if (container instanceof Serializable serializable) {
            serializable.deserialize(arg);
        }
    }
}
