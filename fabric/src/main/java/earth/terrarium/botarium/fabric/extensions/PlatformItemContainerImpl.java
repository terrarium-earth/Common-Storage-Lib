package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.fabric.item.PlatformItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(ItemContainer.class)
public class PlatformItemContainerImpl {

    @ImplementsBaseElement
    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformItemContainer.of(level, pos, state, entity, direction);
    }
}
