package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.Serializable;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.ItemFluidContainer;
import earth.terrarium.botarium.forge.AutoSerializable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeItemFluidContainer(ItemFluidContainer container) implements IFluidHandlerItem, ICapabilityProvider {
    public static final ResourceLocation FLUID_KEY = new ResourceLocation(Botarium.MOD_ID, "fluid_item");

    public ForgeItemFluidContainer(ItemFluidContainer container) {
        this.container = container;
        init();
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container.getContainerItem();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction var2) {
        return capability == ForgeCapabilities.FLUID_HANDLER_ITEM ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public int getTanks() {
        return container.getSize();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return new ForgeFluidHolder(container.getFluids().get(i));
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) container.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return getFluidInTank(i).isFluidEqual(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        init();
        int insertFluid = (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
        if (fluidAction.execute()) setChanged();
        return insertFluid;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        init();
        ForgeFluidHolder forgeFluidHolder = new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate()));
        if (fluidAction.execute()) setChanged();
        return forgeFluidHolder;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        init();
        FluidHolder fluid = this.container.getFluids().get(0).copyHolder();
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        fluid.setAmount(i);
        ForgeFluidHolder forgeFluidHolder = new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate()));
        if (fluidAction.execute()) setChanged();
        return forgeFluidHolder;
    }

    private void setChanged() {
        CompoundTag serialize = this.container.serialize(new CompoundTag());
        getContainer().getOrCreateTag().put(FLUID_KEY.toString(), serialize);
    }

    private void init() {
        this.container.deserialize(this.container.getContainerItem().getOrCreateTag().getCompound(FLUID_KEY.toString()));
    }
}
