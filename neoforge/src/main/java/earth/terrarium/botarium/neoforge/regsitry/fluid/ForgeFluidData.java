package earth.terrarium.botarium.neoforge.regsitry.fluid;

import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import earth.terrarium.botarium.common.registry.fluid.FluidProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class ForgeFluidData implements FluidData {

    private Supplier<? extends FlowingFluid> stillFluid;
    private Supplier<? extends FlowingFluid> flowingFluid;
    private Supplier<? extends Item> bucket;
    private Supplier<? extends LiquidBlock> block;
    private final FluidInformation information;
    private final Supplier<BotariumFluidType> type;

    public ForgeFluidData(Supplier<BotariumFluidType> type, FluidProperties properties) {
        this.type = type;
        this.information = properties;
    }

    public ForgeFluidData(Supplier<BotariumFluidType> type, FluidInformation information) {
        this.type = type;
        this.information = information;
    }

    public Supplier<BotariumFluidType> getType() {
        return type;
    }

    @Override
    public FluidProperties getProperties() {
        return this.information.toProperties();
    }

    @Override
    public FluidInformation getInformation() {
        return this.information;
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
