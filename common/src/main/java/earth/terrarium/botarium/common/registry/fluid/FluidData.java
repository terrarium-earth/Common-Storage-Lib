package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;
import java.util.function.Supplier;

public interface FluidData {

    /**
     * @return The properties of this fluid.
     * @deprecated Use {@link #getInformation()} instead, this method will be removed in the future.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    FluidProperties getProperties();

    FluidInformation getInformation();

    Supplier<? extends FlowingFluid> getStillFluid();

    Supplier<? extends FlowingFluid> getFlowingFluid();

    Supplier<? extends Item> getBucket();

    Supplier<? extends LiquidBlock> getBlock();

    void setStillFluid(Supplier<? extends FlowingFluid> still);

    void setFlowingFluid(Supplier<? extends FlowingFluid> flowing);

    void setBucket(Supplier<? extends Item> bucket);

    void setBlock(Supplier<? extends LiquidBlock> block);

    default Optional<Item> getOptionalBucket() {
        return Optional.ofNullable(getBucket()).map(Supplier::get);
    }

    default Optional<? extends LiquidBlock> getOptionalBlock() {
        return Optional.ofNullable(getBlock()).map(Supplier::get);
    }

    default Optional<? extends FlowingFluid> getOptionalFlowingFluid() {
        return Optional.ofNullable(getFlowingFluid()).map(Supplier::get);
    }

    default Optional<? extends FlowingFluid> getOptionalStillFluid() {
        return Optional.ofNullable(getStillFluid()).map(Supplier::get);
    }
}
