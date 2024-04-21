package earth.terrarium.botarium.common.item;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemProvider {
    private ItemProvider() {}

    public interface Block {
        UnitContainer<ItemUnit> getItems(Level level, BlockPos pos, @Nullable BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity entity, @Nullable Direction direction);
    }

    public interface BlockEntity {
        UnitContainer<ItemUnit> getItems(@Nullable Direction direction);
    }

    public interface Entity {
        UnitContainer<ItemUnit> getItems();
    }

    public interface AutomationEntity {
        UnitContainer<ItemUnit> getItems(@Nullable Direction entity);
    }

    public interface Item {
        UnitContainer<ItemUnit> getItems(ItemStack stack, ItemContext context);
    }
}
