package earth.terrarium.botarium.common.item.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BotariumItemBlock<T extends ItemContainer & Updatable> {

    @Nullable
    T getItemContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
}
