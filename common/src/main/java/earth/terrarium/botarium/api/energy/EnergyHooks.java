package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

public class EnergyHooks {

    @ImplementedByExtension
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    @ImplementedByExtension
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    @ImplementedByExtension
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    @ImplementedByExtension
    public static boolean isEnergyContainer(BlockEntity stack, Direction direction) {
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

    public static Optional<PlatformEnergyManager> safeGetItemEnergyManager(BlockEntity entity, Direction direction) {
        return isEnergyContainer(entity, direction) ? Optional.of(getBlockEnergyManager(entity, direction)) : Optional.empty();
    }

    public static Optional<PlatformEnergyManager> safeGetBlockEnergyManager(ItemStack stack) {
        return isEnergyItem(stack) ? Optional.of(getItemHandler(stack)) : Optional.empty();
    }
}
