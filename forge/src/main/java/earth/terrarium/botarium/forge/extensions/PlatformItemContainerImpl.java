package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.item.ItemContainer;
import earth.terrarium.botarium.forge.item.ForgeItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(ItemContainer.class)
public class PlatformItemContainerImpl {

    @ImplementsBaseElement
    static ItemContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        if (entity == null) entity = level.getBlockEntity(pos);
        if (entity != null) {
            return entity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).map(ForgeItemContainer::new).orElse(null);
        }
        return null;
    }
}
