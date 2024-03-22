package earth.terrarium.botarium.neoforge.item;

import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.neoforge.generic.NeoForgeBlockContainerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ForgeItemApiBlockLookup implements BlockContainerLookup<ItemContainer, @Nullable Direction> {
    public static final ForgeItemApiBlockLookup INSTANCE = new ForgeItemApiBlockLookup();

    private final NeoForgeBlockContainerLookup<IItemHandler, Direction> lookup = new NeoForgeBlockContainerLookup<>(Capabilities.ItemHandler.BLOCK);

    @Override
    public @Nullable ItemContainer find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformItemContainer.of(lookup.find(level, pos, state, entity, direction));
    }

    @Override
    public void registerBlocks(BlockGetter<ItemContainer, @Nullable Direction> getter, Supplier<Block>... containers) {
        lookup.registerBlocks((level, pos, state, entity, direction) -> ForgeItemContainer.of(getter.getContainer(level, pos, state, entity, direction)), containers);
    }

    @Override
    public void registerBlockEntities(BlockGetter<ItemContainer, @Nullable Direction> getter, Supplier<BlockEntityType<?>>... containers) {
        lookup.registerBlockEntities((level, pos, state, entity, direction) -> ForgeItemContainer.of(getter.getContainer(level, pos, state, entity, direction)), containers);
    }
}
