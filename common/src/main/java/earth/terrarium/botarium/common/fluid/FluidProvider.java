package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class FluidProvider {
    private FluidProvider() {}

    public interface Block {
        UnitContainer<FluidUnit> getFluids(Level level, BlockPos pos, @Nullable BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity entity, @Nullable Direction direction);
    }

    public interface BlockEntity {
        UnitContainer<FluidUnit> getFluids(@Nullable Direction direction);
    }

    public interface Entity {
        UnitContainer<FluidUnit> getFluids(@Nullable Direction direction);
    }

    public interface Item {
        UnitContainer<FluidUnit> getFluids(ItemStack stack, ItemContext context);
    }
}
