package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BotariumFlowingFluid extends FlowingFluid {

    @ImplementedByExtension
    public BotariumFlowingFluid(FluidData data) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public FluidData getData() {
        throw new NotImplementedException();
    }

    @Override
    public Fluid getFlowing() {
        return getData().getFlowingFluid().get();
    }

    @Override
    public Fluid getSource() {
        return getData().getStillFluid().get();
    }

    @Override
    protected boolean canConvertToSource() {
        return getData().getProperties().canConvertToSource();
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        Block.dropResources(state, level, pos, state.hasBlockEntity() ? level.getBlockEntity(pos) : null);
    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader level) {
        return getData().getProperties().slopeFindDistance();
    }

    @Override
    protected int getDropOff(@NotNull LevelReader level) {
        return getData().getProperties().dropOff();
    }

    @Override
    public Item getBucket() {
        return getData().getOptionalBucket().orElse(Items.AIR);
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return direction == Direction.DOWN && !isSame(fluid);
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return getData().getProperties().tickDelay();
    }

    @Override
    protected float getExplosionResistance() {
        return getData().getProperties().explosionResistance();
    }

    @Override
    protected BlockState createLegacyBlock(@NotNull FluidState state) {
        return getData().getOptionalBlock()
            .map(Block::defaultBlockState)
            .map(block -> block.setValue(LiquidBlock.LEVEL, getLegacyLevel(state)))
            .orElse(Blocks.AIR.defaultBlockState());
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return false;
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    @Override
    public int getAmount(@NotNull FluidState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean isSame(@NotNull Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        SoundEvent event = getData().getProperties().sounds().getSound("bucket_fill");
        if (event == null) event = SoundEvents.BUCKET_FILL;
        return Optional.ofNullable(event);
    }
}
