package earth.terrarium.botarium.fluid.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.fluid.wrappers.NeoFluidContainer;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidBlockLookup implements BlockLookup<CommonStorage<FluidResource>, Direction>, RegistryEventListener<BlockEntity> {
    public static final FluidBlockLookup INSTANCE = new FluidBlockLookup();
    public static final ResourceLocation NAME = new ResourceLocation(Botarium.MOD_ID, "fluid_block");
    private final List<Consumer<BlockRegistrar<CommonStorage<FluidResource>, Direction>>> registrars = new ArrayList<>();

    private FluidBlockLookup() {
        RegistryEventListener.registerBlock(this);
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(BlockEntity block, @Nullable Direction direction) {
        return null;
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<CommonStorage<FluidResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<BlockEntity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (BlockEntityType<?> blockEntityType : blockEntityTypes) {
                if (blockEntityType == event.getObject().getType()) {
                    event.addCapability(NAME, new FluidCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class FluidCap implements ICapabilityProvider {
        private final BlockEntityGetter<CommonStorage<FluidResource>, Direction> getter;
        private final BlockEntity blockEntity;

        public FluidCap(BlockEntityGetter<CommonStorage<FluidResource>, Direction> getter, BlockEntity blockEntity) {
            this.getter = getter;
            this.blockEntity = blockEntity;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            CommonStorage<FluidResource> storage = getter.getContainer(blockEntity, arg);
            LazyOptional<IFluidHandler> of = LazyOptional.of(() -> new NeoFluidContainer(storage));
            return capability.orEmpty(ForgeCapabilities.ENERGY, of.cast()).cast();
        }
    }
}
