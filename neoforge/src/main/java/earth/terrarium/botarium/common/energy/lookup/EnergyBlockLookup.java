package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.common.energy.wrappers.CommonEnergyContainer;
import earth.terrarium.botarium.common.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnergyBlockLookup implements BlockLookup<LongContainer, Direction> {
    List<Consumer<BlockRegistrar<LongContainer, Direction>>> registrars;

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @Nullable LongContainer find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction);
        if (storage instanceof NeoEnergyContainer(LongContainer container)) {
            return container;
        }
        return storage == null ? null : new CommonEnergyContainer(storage);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<LongContainer, Direction>> registrar) {
        registrars.add(registrar);
    }
}
