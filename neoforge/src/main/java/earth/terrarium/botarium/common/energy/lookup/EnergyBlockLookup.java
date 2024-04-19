package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.common.energy.wrappers.CommonEnergyContainer;
import earth.terrarium.botarium.common.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnergyBlockLookup implements BlockLookup<LongContainer, Direction>, CapabilityRegisterer {
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

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public static class EventRegistrar implements BlockLookup.BlockRegistrar<LongContainer, Direction> {
        RegisterCapabilitiesEvent event;

        public EventRegistrar(RegisterCapabilitiesEvent event) {
            this.event = event;
        }

        @Override
        public void registerBlocks(BlockLookup.BlockGetter<LongContainer, Direction> getter, Block... containers) {
            for (Block block : containers) {
                event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, pos, state, entity, direction) -> {
                    LongContainer storage = getter.getContainer(level, pos, state, entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockLookup.BlockEntityGetter<LongContainer, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntity : containers) {
                event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntity, (entity, direction) -> {
                    LongContainer storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                });
            }
        }
    }
}
