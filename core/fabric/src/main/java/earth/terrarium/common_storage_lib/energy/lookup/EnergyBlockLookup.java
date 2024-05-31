package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.common.CommonValueStorage;
import earth.terrarium.common_storage_lib.storage.fabric.FabricLongStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Consumer;

public class EnergyBlockLookup implements BlockLookup<ValueStorage, @Nullable Direction> {

    @Override
    public @Nullable ValueStorage find(BlockEntity block, @Nullable Direction direction) {
        EnergyStorage storage = EnergyStorage.SIDED.find(block.getLevel(), block.getBlockPos(), direction);
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricLongStorage(ValueStorage rootContainer, var ignored)) {
            return rootContainer;
        }
        return new CommonValueStorage(storage);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, @Nullable Direction>> registrar) {
        registrar.accept((getter, blockEntityTypes) -> EnergyStorage.SIDED.registerForBlockEntities((entity, context) -> {
            ValueStorage container = getter.getContainer(entity, context);
            return new FabricLongStorage(container);
        }, blockEntityTypes));
    }
}
