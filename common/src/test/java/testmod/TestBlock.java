package testmod;

import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class TestBlock extends BaseEntityBlock {
    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(TestBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TestBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isShiftKeyDown()
                && level.getBlockEntity(blockPos) instanceof TestBlockEntity testBlockEntity
                && testBlockEntity.getFluidContainer(level, blockPos, blockState, testBlockEntity, null).container() instanceof SimpleFluidContainer fluidContainer
        ) {
            fluidContainer.clearContent();
        }

        if (!level.isClientSide()) {
            player.sendSystemMessage(Component.literal("Energy: " + EnergyContainer.of(
                    level.getBlockEntity(blockPos),
                    blockHitResult.getDirection()
            ).getStoredEnergy()));

            if (level.getBlockEntity(blockPos) instanceof TestBlockEntity testBlockEntity) {
                player.sendSystemMessage(Component.literal("Fluid: " + FluidContainer.of(level, blockPos, blockState, testBlockEntity, null).getFluids().stream()
                        .mapToLong(FluidHolder::getFluidAmount)
                        .mapToObj(Long::toString)
                        .collect(Collectors.joining(", "))
                ));
            }

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            ManaContainer container = TestMod.MANA_LOOKUP_BLOCK.getContainer(level, blockPos, blockState, blockEntity, null);
            if (container != null) {
                player.sendSystemMessage(Component.literal("Mana: " + container.getStoredAmount()));
                container.insert(100, false);
                if (blockEntity != null) blockEntity.setChanged();
                level.sendBlockUpdated(blockPos, blockState, blockState, TestBlock.UPDATE_ALL);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, TestMod.EXAMPLE_BLOCK_ENTITY.get(), (level1, blockPos, blockState1, blockEntity) -> ((TestBlockEntity) blockEntity).tick());
    }
}
