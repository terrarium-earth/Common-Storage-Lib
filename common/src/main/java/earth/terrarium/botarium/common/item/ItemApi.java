package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.item.base.BotariumItemBlock;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemApi {

    // region Item Container Registration
    private static final Map<Supplier<BlockEntityType<?>>, BotariumItemBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BotariumItemBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, BotariumItemBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, BotariumItemBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;

    public static Map<BlockEntityType<?>, BotariumItemBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP, "Item containing block entity");
    }

    public static Map<Block, BotariumItemBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP, "Item containing block");
    }

    public static BotariumItemBlock<?> getItemBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static BotariumItemBlock<?> getItemBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    /**
     * Retrieves the Botarium specific ItemContainer object from a block entity or block. This method is used internally
     * by the Botarium API and should not be used by other mods.
     *
     * @param <T>       the type of ItemContainer
     * @param level     the game level
     * @param pos       the position of the block
     * @param state     the block state
     * @param entity    the block entity (can be null)
     * @param direction the direction (can be null)
     * @return the API ItemContainer object
     */
    public static <T extends ItemContainer & Updatable> T getAPIItemContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        BotariumItemBlock<?> getter = getItemBlock(state.getBlock());
        if (getter == null && entity != null) {
            getter = getItemBlock(entity.getType());

            if (getter == null && entity instanceof BotariumItemBlock<?> itemGetter) {
                getter = itemGetter;
            }

            if (getter == null && state.getBlock() instanceof BotariumItemBlock<?> itemGetter) {
                getter = itemGetter;
            }
        }
        if (getter == null) return null;
        return (T) getter.getItemContainer(level, pos, state, entity, direction);
    }

    public static void registerItemBlockEntity(Supplier<BlockEntityType<?>> block, BotariumItemBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerItemBlockEntity(BotariumItemBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerItemBlock(Supplier<Block> block, BotariumItemBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerItemBlock(BotariumItemBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }
}
