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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class TestBlockNonInterface extends Block {
    public TestBlockNonInterface(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return simpleCodec(TestBlockNonInterface::new);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        EnergyContainer container = EnergyContainer.of(level, blockPos, blockState, null, null);
        FluidContainer fluidContainer = FluidContainer.of(level, blockPos, blockState, null, null);
        if (player.isShiftKeyDown()
            && container != null
            && fluidContainer != null
        ) {
            fluidContainer.clearContent();
        }

        if (!level.isClientSide() && container != null && fluidContainer != null) {
            player.sendSystemMessage(Component.literal("Energy: " + container.getStoredEnergy()));
            player.sendSystemMessage(Component.literal("Fluid: " + fluidContainer.getFluids().stream().reduce(0L, (a, b) -> a + b.getFluidAmount(), Long::sum)));
        }
        return InteractionResult.SUCCESS;
    }
}
