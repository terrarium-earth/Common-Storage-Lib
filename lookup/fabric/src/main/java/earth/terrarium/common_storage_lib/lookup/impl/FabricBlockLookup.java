package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricBlockLookup<T, C> implements BlockLookup<T, C> {
    private final BlockApiLookup<T, C> lookup;

    public FabricBlockLookup(BlockApiLookup<T, C> lookup) {
        this.lookup = lookup;
    }

    public FabricBlockLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(BlockApiLookup.get(id, type, contextType));
    }

    @Override
    public @Nullable T find(BlockEntity block, @Nullable C direction) {
        return lookup.find(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<T, C>> registrar) {
        registrar.accept((getter, blockEntityTypes) -> lookup.registerForBlockEntities(getter::getContainer, blockEntityTypes));
    }
}
