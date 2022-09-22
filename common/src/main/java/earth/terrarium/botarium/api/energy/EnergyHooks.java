package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyHooks {


    /**
     * Gets the energy container of the given itemstack.
     * Will throw error if the itemstack is not an energy container.
     *
     * @deprecated Use {@link EnergyHooks#getItemEnergyManager(ItemStack)} instead.
     * @param stack Gets the energy container from the item stack
     * @return The energy container
     */
    @Deprecated
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        return getItemEnergyManager(stack);
    }

    @ImplementedByExtension
    public static PlatformEnergyManager getItemEnergyManager(ItemStack stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    @ImplementedByExtension
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    @ImplementedByExtension
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    @ImplementedByExtension
    public static boolean isEnergyContainer(BlockEntity stack, @Nullable Direction direction) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    public static long moveEnergy(PlatformEnergyManager from, PlatformEnergyManager to, long amount) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(extracted, true);
        from.extract(inserted, false);
        return to.insert(inserted, false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveEnergy(Optional<PlatformEnergyManager> from, Optional<PlatformEnergyManager> to, long amount) {
        return from.map(f -> to.map(t -> moveEnergy(f, t, amount)).orElse(0L)).orElse(0L);
    }

    public static Optional<PlatformEnergyManager> safeGetBlockEnergyManager(BlockEntity entity, @Nullable Direction direction) {
        return isEnergyContainer(entity, direction) ? Optional.of(getBlockEnergyManager(entity, direction)) : Optional.empty();
    }

    public static Optional<PlatformEnergyManager> safeGetItemEnergyManager(ItemStack stack) {
        return isEnergyItem(stack) ? Optional.of(getItemEnergyManager(stack)) : Optional.empty();
    }

    public static long moveItemToItemEnergy(ItemStack from, ItemStack to, long amount) {
        return safeMoveEnergy(safeGetItemEnergyManager(from), safeGetItemEnergyManager(to), amount);
    }

    public static long moveBlockToBlockEnergy(BlockEntity from, @Nullable Direction fromDirection, BlockEntity to, @Nullable Direction toDirection, long amount) {
        return safeMoveEnergy(safeGetBlockEnergyManager(from, fromDirection), safeGetBlockEnergyManager(to, toDirection), amount);
    }

    public static long moveBlockToBlockEnergy(BlockEntity from, BlockEntity to, long amount) {
        return safeMoveEnergy(safeGetBlockEnergyManager(from, null), safeGetBlockEnergyManager(to, null), amount);
    }

    public static long moveBlockToItemEnergy(BlockEntity from, @Nullable Direction fromDirection, ItemStack to, long amount) {
        return safeMoveEnergy(safeGetBlockEnergyManager(from, fromDirection), safeGetItemEnergyManager(to), amount);
    }

    public static long moveItemToBlockEnergy(ItemStack from, BlockEntity to, @Nullable Direction toDirection, long amount) {
        return safeMoveEnergy(safeGetItemEnergyManager(from), safeGetBlockEnergyManager(to, toDirection), amount);
    }
}
