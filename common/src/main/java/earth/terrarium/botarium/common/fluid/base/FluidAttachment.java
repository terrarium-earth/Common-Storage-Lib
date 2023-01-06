package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface FluidAttachment<U, T extends FluidContainer & Updatable<U>> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    T getFluidContainer(U holder);

    Class<U> getFluidHolderType();

    interface Item extends FluidAttachment<ItemStack, WrappedItemFluidContainer> {
        @Override
        default Class<ItemStack> getFluidHolderType() {
            return ItemStack.class;
        }
    }
    interface Block extends FluidAttachment<BlockEntity, WrappedBlockFluidContainer> {
        @Override
        default Class<BlockEntity> getFluidHolderType() {
            return BlockEntity.class;
        }
    }
}
