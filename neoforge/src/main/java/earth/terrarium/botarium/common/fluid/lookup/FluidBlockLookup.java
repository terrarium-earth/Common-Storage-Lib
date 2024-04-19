package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.common.fluid.wrappers.CommonFluidContainer;
import earth.terrarium.botarium.common.fluid.wrappers.NeoFluidContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FluidBlockLookup implements BlockLookup<UnitContainer<FluidUnit>, Direction>, CapabilityRegisterer {
    private final List<Consumer<BlockRegistrar<UnitContainer<FluidUnit>, Direction>>> registrars = new ArrayList<>();

    @Override
    public @Nullable UnitContainer<FluidUnit> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, entity, direction);

        if (handler instanceof NeoFluidContainer(UnitContainer<FluidUnit> container)) {
            return container;
        }

        return handler == null ? null : new CommonFluidContainer(handler);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<UnitContainer<FluidUnit>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public record EventRegistrar(RegisterCapabilitiesEvent event) implements BlockRegistrar<UnitContainer<FluidUnit>, Direction> {
        @Override
        public void registerBlocks(BlockGetter<UnitContainer<FluidUnit>, Direction> getter, net.minecraft.world.level.block.Block... containers) {
            for (net.minecraft.world.level.block.Block block : containers) {
                event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, pos, state, entity, direction) -> {
                    UnitContainer<FluidUnit> container = getter.getContainer(level, pos, state, entity, direction);
                    return container == null ? null : new NeoFluidContainer(container);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<UnitContainer<FluidUnit>, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntityType : containers) {
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (entity, direction) -> {
                    UnitContainer<FluidUnit> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoFluidContainer(container);
                });
            }
        }
    }
}
