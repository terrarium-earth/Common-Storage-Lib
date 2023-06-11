package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface EnergyAttachment<U, T extends EnergyContainer & Updatable<U>> {

    /**
     * @return The {@link EnergyContainer} for the block.
     */
    T getEnergyStorage(U holder);

    Class<U> getEnergyHolderType();

    @Deprecated
    interface Item extends EnergyAttachment<ItemStack, WrappedItemEnergyContainer> {
        @Override
        default Class<ItemStack> getEnergyHolderType() {
            return ItemStack.class;
        }
    }

    @Deprecated
    interface Block extends EnergyAttachment<BlockEntity, WrappedBlockEnergyContainer> {
        @Override
        default Class<BlockEntity> getEnergyHolderType() {
            return BlockEntity.class;
        }
    }
}
