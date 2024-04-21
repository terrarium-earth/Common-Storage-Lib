package earth.terrarium.botarium.common.energy;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class EnergyProvider {
    private EnergyProvider() {}

    public interface Block {
        LongContainer getEnergy(Level level, BlockPos pos, @Nullable BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity entity, @Nullable Direction direction);
    }

    public interface BlockEntity {
        LongContainer getEnergy(@Nullable Direction direction);
    }

    public interface Entity {
        LongContainer getEnergy(@Nullable Direction direction);
    }

    public interface Item {
        LongContainer getEnergy(ItemStack stack, ItemContext context);
    }
}
