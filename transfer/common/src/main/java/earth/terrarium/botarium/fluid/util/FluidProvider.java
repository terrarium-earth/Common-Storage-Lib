package earth.terrarium.botarium.fluid.util;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class FluidProvider {
    private FluidProvider() {}

    public interface Block {
        CommonStorage<FluidResource> getFluids(Level level, BlockPos pos, @Nullable BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity entity, @Nullable Direction direction);
    }

    public interface BlockEntity {
        CommonStorage<FluidResource> getFluids(@Nullable Direction direction);
    }

    public interface Entity {
        CommonStorage<FluidResource> getFluids(@Nullable Direction direction);
    }

    public interface Item {
        CommonStorage<FluidResource> getFluids(ItemStack stack, ItemContext context);
    }
}
