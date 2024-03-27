package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.neoforge.item.PlatformItemContainer;
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
public interface ItemContainerImpl {

    @ImplementsBaseElement
    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IItemHandler capability = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, entity, direction);
        return capability != null ? new PlatformItemContainer(capability) : null;
    }
}
