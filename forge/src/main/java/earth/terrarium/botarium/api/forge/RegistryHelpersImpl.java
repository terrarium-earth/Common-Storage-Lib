package earth.terrarium.botarium.api.forge;

import earth.terrarium.botarium.api.RegistryHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RegistryHelpersImpl {
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }
}
