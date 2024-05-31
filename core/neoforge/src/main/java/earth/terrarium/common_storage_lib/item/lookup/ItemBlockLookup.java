package earth.terrarium.common_storage_lib.item.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.item.wrappers.CommonItemContainer;
import earth.terrarium.common_storage_lib.item.wrappers.NeoItemHandler;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemBlockLookup implements BlockLookup<CommonStorage<ItemResource>, Direction>, RegistryEventListener<BlockEntity> {
    public static final ItemBlockLookup INSTANCE = new ItemBlockLookup();
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "item_block");
    private final List<Consumer<BlockRegistrar<CommonStorage<ItemResource>, Direction>>> registrars = new ArrayList<>();

    private ItemBlockLookup() {
        RegistryEventListener.registerBlock(this);
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(BlockEntity block, @Nullable Direction direction) {
        LazyOptional<IItemHandler> storage = block.getCapability(ForgeCapabilities.ITEM_HANDLER, direction);
        if (storage.isPresent()) {
            IItemHandler itemStorage = storage.orElseThrow(IllegalStateException::new);
            if (itemStorage instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
                return container;
            }
            return new CommonItemContainer(itemStorage);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<CommonStorage<ItemResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<BlockEntity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (BlockEntityType<?> blockEntityType : blockEntityTypes) {
                if (blockEntityType == event.getObject().getType()) {
                    event.addCapability(NAME, new ItemCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class ItemCap implements ICapabilityProvider {
        private final BlockEntityGetter<CommonStorage<ItemResource>, Direction> getter;
        private final BlockEntity blockEntity;

        public ItemCap(BlockEntityGetter<CommonStorage<ItemResource>, Direction> getter, BlockEntity blockEntity) {
            this.getter = getter;
            this.blockEntity = blockEntity;
        }

        @Override
        public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
            CommonStorage<ItemResource> container = getter.getContainer(blockEntity, side);
            LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new NeoItemHandler(container));
            return cap.orEmpty(ForgeCapabilities.ITEM_HANDLER, optional.cast()).cast();
        }
    }
}
