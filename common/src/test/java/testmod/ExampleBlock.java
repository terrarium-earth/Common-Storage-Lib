package testmod;

import earth.terrarium.botarium.api.energy.EnergyHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ExampleBlock extends BaseEntityBlock {
    public ExampleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExampleBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        var blockEnergyManager = EnergyHooks.getBlockEnergyManager(level.getBlockEntity(blockPos), blockHitResult.getDirection());
        player.sendSystemMessage(Component.literal(String.valueOf(blockEnergyManager.getStoredEnergy())));
        return InteractionResult.SUCCESS;
    }
}
