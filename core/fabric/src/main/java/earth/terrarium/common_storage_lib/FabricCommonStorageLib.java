package earth.terrarium.common_storage_lib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class FabricCommonStorageLib implements ModInitializer {
    
    private boolean loaded = false;
    
    @Override
    public void onInitialize() {
        
        // this is required in a later event than initial initialization as registry content might be
        // added by other mods at a later point of initialization
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (loaded) return;
            loaded = true;
            CommonStorageLib.init();
        });
        
    }
    
    @SuppressWarnings({"UnstableApiUsage", "ReferenceToMixin"})
    public static @Nullable BlockEntity getTypeInstance(BlockEntityType<?> type) {
        var blocks = ((BlockEntityTypeAccessor) type).getBlocks();
        if (blocks.isEmpty()) return null;
        return type.create(BlockPos.ZERO, blocks.stream().findFirst().get().defaultBlockState());
    }
}
