package earth.terrarium.botarium.common.menu.base;

import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface EnergyAttachment<U, T extends EnergyContainer & Updatable<U>> {

    /**
     * @return The {@link EnergyContainer} for the block.
     */
    T getEnergyStorage();

    Class<U> getHolderType();

    interface Item extends EnergyAttachment<ItemStack, WrappedItemEnergyContainer> {
        @Override
        default Class<ItemStack> getHolderType() {
            return ItemStack.class;
        }
    }
    interface Block extends EnergyAttachment<BlockEntity, WrappedBlockEnergyContainer> {
        @Override
        default Class<BlockEntity> getHolderType() {
            return BlockEntity.class;
        }
    }
}
