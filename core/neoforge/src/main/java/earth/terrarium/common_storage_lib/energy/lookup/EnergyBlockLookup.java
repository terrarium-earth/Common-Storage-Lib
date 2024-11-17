package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.energy.wrappers.CommonEnergyStorage;
import earth.terrarium.common_storage_lib.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class EnergyBlockLookup implements BlockLookup<ValueStorage, Direction>, RegistryEventListener {
    public static final EnergyBlockLookup INSTANCE = new EnergyBlockLookup();
    private final List<Consumer<BlockRegistrar<ValueStorage, Direction>>> registrars = new ArrayList<>();
    
    private EnergyBlockLookup() {
        registerSelf();
    }
    
    @Override
    @SuppressWarnings("DataFlowIssue")
    public @Nullable ValueStorage find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction);
        if (storage instanceof NeoEnergyContainer(ValueStorage container)) {
            return container;
        }
        return storage == null ? null : new CommonEnergyStorage(storage);
    }
    
    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, Direction>> registrar) {
        registrars.add(registrar);
    }
    
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }
    
    public static class EventRegistrar implements BlockLookup.BlockRegistrar<ValueStorage, Direction> {
        RegisterCapabilitiesEvent event;
        
        public EventRegistrar(RegisterCapabilitiesEvent event) {
            this.event = event;
        }
        
        @Override
        public void registerBlocks(BlockLookup.BlockGetter<ValueStorage, Direction> getter, Block... containers) {
            for (Block block : containers) {
                event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, pos, state, entity, direction) -> {
                    ValueStorage storage = getter.getContainer(level, pos, state, entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                }, block);
            }
        }
        
        @SuppressWarnings({"OptionalGetWithoutIsPresent"})
        @Override
        public void registerBlockEntities(BlockLookup.BlockEntityGetter<ValueStorage, Direction> getter, BlockEntityType<?> container, Predicate<BlockEntity> blockPredicate) {
            
            if (container.getValidBlocks().isEmpty()
                  || !blockPredicate.test(container.create(BlockPos.ZERO, container.getValidBlocks().stream().findFirst().get().defaultBlockState()))) return;
            
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, container, (entity, direction) -> {
                ValueStorage storage = getter.getContainer(entity, direction);
                return storage == null ? null : new NeoEnergyContainer(storage);
            });
        }
    }
}
