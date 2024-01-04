package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.neoforge.fluid.PlatformBlockFluidHandler;
import earth.terrarium.botarium.neoforge.fluid.PlatformFluidItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
@ClassExtension(FluidApi.class)
public class FluidApiImpl {
    @ImplementsBaseElement
    public static ItemFluidContainer getItemFluidContainer(ItemStackHolder stack) {
        return isFluidContainingItem(stack.getStack()) ? new PlatformFluidItemHandler(stack) : null;
    }

    @ImplementsBaseElement
    public static FluidContainer getBlockFluidContainer(BlockEntity entity, @Nullable Direction direction) {
        return isFluidContainingBlock(entity, direction) ? new PlatformBlockFluidHandler(entity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, entity.getBlockPos(), direction)) : null;
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        return entity.getLevel() != null && entity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, entity.getBlockPos(), direction) != null;
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingItem(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
    }
}
