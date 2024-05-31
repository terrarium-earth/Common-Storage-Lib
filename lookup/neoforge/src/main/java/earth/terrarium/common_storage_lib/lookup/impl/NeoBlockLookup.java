package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoBlockLookup<T, C> implements BlockLookup<T, C>, RegistryEventListener<BlockEntity> {
    private final List<Consumer<BlockLookup.BlockRegistrar<T, C>>> registrars = new ArrayList<>();
    private final Capability<BlockEntityGetter<T, C>> capability;
    private final ResourceLocation id;

    public NeoBlockLookup(ResourceLocation id) {
        this.id = id;
        this.capability = CapabilityManager.get(new CapabilityToken<>(){});
    }

    @Override
    public @Nullable T find(BlockEntity block, @Nullable C direction) {
        return block.getCapability(capability).map(getter -> getter.getContainer(block, direction)).orElse(null);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<BlockEntity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (BlockEntityType<?> blockEntityType : blockEntityTypes) {
                if (blockEntityType == event.getObject().getType()) {
                    event.addCapability(id, new BlockCapability(getter));
                    return;
                }
            }
        }));
    }

    public class BlockCapability implements ICapabilityProvider {
        private final BlockEntityGetter<T, C> getter;

        public BlockCapability(BlockEntityGetter<T, C> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <X> LazyOptional<X> getCapability(@NotNull Capability<X> capability, @Nullable Direction arg) {
            return capability.orEmpty(NeoBlockLookup.this.capability, LazyOptional.of(() -> getter).cast()).cast();
        }
    }
}