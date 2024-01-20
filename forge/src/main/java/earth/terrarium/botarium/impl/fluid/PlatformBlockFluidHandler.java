package earth.terrarium.botarium.impl.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidSnapshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record PlatformBlockFluidHandler(IFluidHandler handler) implements FluidContainer {

    @Nullable
    public static PlatformBlockFluidHandler of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        ICapabilityProvider provider = entity != null ? entity : level.getBlockEntity(pos);
        if (provider == null) return null;
        var handler = provider.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
        return handler.map(PlatformBlockFluidHandler::new).orElse(null);
    }

    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        return handler.fill(new ForgeFluidHolder(fluid).getFluidStack(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        return new ForgeFluidHolder(handler.drain(new ForgeFluidHolder(fluid).getFluidStack(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        FluidStack fluidInTank = handler.getFluidInTank(slot);
        handler.drain(fluidInTank, IFluidHandler.FluidAction.EXECUTE);
        handler.fill(new ForgeFluidHolder(fluid).getFluidStack(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public List<FluidHolder> getFluids() {
        List<FluidHolder> fluids = new ArrayList<>();
        for (int i = 0; i < handler.getTanks(); i++) {
            fluids.add(new ForgeFluidHolder(handler.getFluidInTank(i)));
        }
        return fluids;
    }

    @Override
    public int getSize() {
        return handler.getTanks();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < handler.getTanks(); i++) {
            if (!handler.getFluidInTank(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public FluidContainer copy() {
        throw new UnsupportedOperationException("Copying is not supported on PlatformFluidContainers");
    }

    @Override
    public long getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public void fromContainer(FluidContainer container) {
        throw new UnsupportedOperationException("Copying is not supported on PlatformFluidContainers");
    }

    @Override
    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public boolean isFluidValid(int slot, FluidHolder fluidHolder) {
        return handler.isFluidValid(slot, new ForgeFluidHolder(fluidHolder).getFluidStack());
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < handler.getTanks(); i++) {
            handler.drain(handler.getFluidInTank(i), IFluidHandler.FluidAction.EXECUTE);
        }
    }
}
