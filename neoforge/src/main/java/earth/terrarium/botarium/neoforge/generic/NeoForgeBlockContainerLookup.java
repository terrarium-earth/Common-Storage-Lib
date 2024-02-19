package earth.terrarium.botarium.neoforge.generic;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.generic.base.BlockContainerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeBlockContainerLookup<T, C> implements BlockContainerLookup<T, C> {
    private final ResourceLocation name;
    private final BlockCapability<T, @Nullable C> capability;
    private final Map<Supplier<Block>, BlockGetter<T, C>> blockGetterMap = new HashMap<>();
    private final Map<Supplier<BlockEntityType<?>>, BlockGetter<T, C>> blockEntityGetterMap = new HashMap<>();
    private Map<Block, BlockGetter<T, C>> blockMap = null;
    private Map<BlockEntityType<?>, BlockGetter<T, C>> blockEntityMap = null;

    public NeoForgeBlockContainerLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        this.name = name;
        capability = BlockCapability.create(name, typeClass, contextClass);
    }

    @Override
    public @Nullable T getContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return level.getCapability(capability, pos, state, entity, direction);
    }

    @Override
    public void registerBlocks(BlockGetter<T, C> getter, Supplier<Block>... containers) {
        for (Supplier<Block> container : containers) {
            blockGetterMap.put(container, getter);
        }
    }

    @Override
    public void registerBlockEntities(BlockGetter<T, C> getter, Supplier<BlockEntityType<?>>... containers) {
        for (Supplier<BlockEntityType<?>> container : containers) {
            blockEntityGetterMap.put(container, getter);
        }
    }

    public void registerWithCaps(RegisterCapabilitiesEvent event) {
        getBlockMap().forEach((block, blockGetter) -> event.registerBlock(capability, blockGetter::getContainer, block));
        getBlockEntityMap().forEach((blockEntityType, blockGetter) -> event.registerBlockEntity(capability, blockEntityType, (object, object2) -> blockGetter.getContainer(object.getLevel(), object.getBlockPos(), object.getBlockState(), object, object2)));
    }

    public Map<Block, BlockGetter<T, C>> getBlockMap() {
        blockMap = Botarium.finalizeRegistration(blockGetterMap, blockMap, name.getNamespace() + " " + name.getPath() + " block");
        return blockMap;
    }

    public Map<BlockEntityType<?>, BlockGetter<T, C>> getBlockEntityMap() {
        blockEntityMap = Botarium.finalizeRegistration(blockEntityGetterMap, blockEntityMap, name.getNamespace() + " " + name.getPath() + " block entity");
        return blockEntityMap;
    }

    BlockCapability<T, @Nullable C> getCapability() {
        return capability;
    }
}
