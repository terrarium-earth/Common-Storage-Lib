package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.energy.PlatformEnergyManager;
import earth.terrarium.botarium.fabric.energy.PlatformItemEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

@ClassExtension(EnergyApi.class)
public class EnergyApiImpl {

    /**
     * Gets the energy container of the given {@link ItemStack}.
     * Will throw error if the {@link ItemStack} is not an energy container.
     *
     * @param stack The {@link ItemStack} to get the energy container from.
     * @return The {@link EnergyContainer} of the given {@link ItemStack}.
     */
    @ImplementsBaseElement
    @Nullable
    public static EnergyContainer getItemEnergyContainer(ItemStackHolder stack) {
        return EnergyApi.isEnergyItem(stack.getStack()) ? new PlatformItemEnergyManager(stack) : null;
    }

    /**
     * Gets the energy container of the given {@link BlockEntity} and {@link Direction}.
     * Will throw error if the {@link BlockEntity} is not an energy container.
     *
     * @param entity    The {@link BlockEntity} to get the energy container from.
     * @param direction The {@link Direction} to get the energy container from.
     * @return The {@link EnergyContainer} of the given {@link BlockEntity} and {@link Direction}.
     */
    @ImplementsBaseElement
    @Nullable
    public static EnergyContainer getBlockEnergyContainer(BlockEntity entity, @Nullable Direction direction) {
        return EnergyApi.isEnergyBlock(entity, direction) ? new PlatformEnergyManager(entity, direction) : null;
    }

    /**
     * @param stack The {@link ItemStack} to check if it is an energy container.
     * @return Whether the given {@link ItemStack} is an energy container.
     */
    @ImplementsBaseElement
    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorageUtil.isEnergyStorage(stack);
    }

    /**
     * @param stack     The {@link BlockEntity} to check if it is an energy container.
     * @param direction The {@link Direction} to check for an energy container on the {@link BlockEntity}.
     * @return Whether the given {@link BlockEntity} is an energy container.
     */
    @ImplementsBaseElement
    public static boolean isEnergyBlock(BlockEntity block, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(block.getLevel(), block.getBlockPos(), direction) != null;
    }
}
