package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.energy.wrappers.CommonEnergyStorage;
import earth.terrarium.common_storage_lib.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyBlockLookup implements BlockLookup<ValueStorage, Direction>, RegistryEventListener<BlockEntity> {
    public static final EnergyBlockLookup INSTANCE = new EnergyBlockLookup();
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "energy_block");
    private final List<Consumer<BlockRegistrar<ValueStorage, Direction>>> registrars = new ArrayList<>();

    private EnergyBlockLookup() {
        RegistryEventListener.registerBlock(this);
    }

    @Override
    public @Nullable ValueStorage find(BlockEntity block, @Nullable Direction direction) {
        LazyOptional<IEnergyStorage> storage = block.getCapability(ForgeCapabilities.ENERGY, direction);
        if (storage.isPresent()) {
            IEnergyStorage energyStorage = storage.orElseThrow(IllegalStateException::new);
            if (energyStorage instanceof NeoEnergyContainer(ValueStorage container)) {
                return container;
            }
            return new CommonEnergyStorage(energyStorage);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<BlockEntity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (BlockEntityType<?> blockEntityType : blockEntityTypes) {
                if (blockEntityType == event.getObject().getType()) {
                    event.addCapability(NAME, new EnergyCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class EnergyCap implements ICapabilityProvider {
        private final BlockEntityGetter<ValueStorage, Direction> getter;
        private final BlockEntity entity;

        public EnergyCap(BlockEntityGetter<ValueStorage, Direction> getter, BlockEntity entity) {
            this.getter = getter;
            this.entity = entity;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            ValueStorage storage = getter.getContainer(entity, arg);
            LazyOptional<IEnergyStorage> of = LazyOptional.of(() -> new NeoEnergyContainer(storage));
            return capability.orEmpty(ForgeCapabilities.ENERGY, of.cast()).cast();
        }
    }
}
