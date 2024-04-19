package earth.terrarium.botarium.common.item.lookup;

import earth.terrarium.botarium.common.item.wrappers.CommonItemContainer;
import earth.terrarium.botarium.common.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemBlockLookup implements BlockLookup<UnitContainer<ItemUnit>, Direction>, CapabilityRegisterer {
    private final List<Consumer<BlockRegistrar<UnitContainer<ItemUnit>, Direction>>> registrars = new ArrayList<>();
    @Override
    public @Nullable UnitContainer<ItemUnit> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, entity, direction);
        if (handler instanceof NeoItemHandler(UnitContainer<ItemUnit> container)) {
            return container;
        }
        return handler == null ? null : new CommonItemContainer(handler);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<UnitContainer<ItemUnit>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public record EventRegistrar(RegisterCapabilitiesEvent event) implements BlockRegistrar<UnitContainer<ItemUnit>, Direction> {
        @Override
        public void registerBlocks(BlockGetter<UnitContainer<ItemUnit>, Direction> getter, net.minecraft.world.level.block.Block... containers) {
            for (net.minecraft.world.level.block.Block block : containers) {
                event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, entity, direction) -> {
                    UnitContainer<ItemUnit> container = getter.getContainer(level, pos, state, entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<UnitContainer<ItemUnit>, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntityType : containers) {
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (entity, direction) -> {
                    UnitContainer<ItemUnit> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                });
            }
        }
    }
}
