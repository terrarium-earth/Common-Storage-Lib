package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.ItemFluidContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeItemFluidContainer extends ForgeFluidContainer implements IFluidHandlerItem {
    public final ItemFluidContainer container;

    public ForgeItemFluidContainer(ItemFluidContainer container) {
        super(container);
        this.container = container;
    }

    @Override
    public @NotNull ItemStack getContainer() {
        return container.getContainerItem();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction var2) {
        return capability == ForgeCapabilities.FLUID_HANDLER_ITEM ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }
}
