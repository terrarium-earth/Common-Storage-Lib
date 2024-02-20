package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.item.ItemContainer;
import earth.terrarium.botarium.neoforge.item.ForgeItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

@ClassExtension(ItemContainer.class)
public class PlatformItemContainerImpl {

    @ImplementsBaseElement
    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IItemHandler capability = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, entity, direction);
        return capability != null ? new ForgeItemContainer(capability) : null;
    }
}