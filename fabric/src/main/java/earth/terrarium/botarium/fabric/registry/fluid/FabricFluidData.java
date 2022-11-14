package earth.terrarium.botarium.fabric.registry.fluid;

import earth.terrarium.botarium.api.registry.fluid.FluidProperties;
import earth.terrarium.botarium.api.registry.fluid.FluidData;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class FabricFluidData implements FluidData {

    private Supplier<? extends FlowingFluid> stillFluid;
    private Supplier<? extends FlowingFluid> flowingFluid;
    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;
    private final FluidProperties properties;

    public FabricFluidData(FluidProperties properties) {
        this.properties = properties;
    }

    @Override
    public FluidProperties getProperties() {
        return this.properties;
    }

    @Override
    public Supplier<? extends FlowingFluid> getStillFluid() {
        return this.stillFluid;
    }

    @Override
    public Supplier<? extends FlowingFluid> getFlowingFluid() {
        return this.flowingFluid;
    }

    @Override
    public Supplier<? extends Item> getBucket() {
        return this.bucket;
    }

    @Override
    public Supplier<? extends LiquidBlock> getBlock() {
        return this.block;
    }

    @Override
    public void setStillFluid(Supplier<? extends FlowingFluid> still) {
        this.stillFluid = still;
    }

    @Override
    public void setFlowingFluid(Supplier<? extends FlowingFluid> flowing) {
        this.flowingFluid = flowing;
    }

    @Override
    public void setBucket(Supplier<? extends Item> bucket) {
        this.bucket = bucket;
    }

    @Override
    public void setBlock(Supplier<? extends LiquidBlock> block) {
        this.block = block;
    }
}
