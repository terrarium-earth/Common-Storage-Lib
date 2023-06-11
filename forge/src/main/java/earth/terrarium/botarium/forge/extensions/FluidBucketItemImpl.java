package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.registry.fluid.FluidBucketItem;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidBucketItem.class)
public class FluidBucketItemImpl extends BucketItem {

    @ExtensionInjectedElement
    private final FluidData data;

    @ImplementsBaseElement
    public FluidBucketItemImpl(FluidData data, Properties properties) {
        super(() -> data.getStillFluid().get(), properties);
        this.data = data;
        data.setBucket(() -> this);
    }

    @ImplementsBaseElement
    public FluidData getData() {
        return data;
    }

    @Override
    public ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}

