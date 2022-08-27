package earth.terrarium.botarium.api.fabric;

import earth.terrarium.botarium.api.RegistryHelpers;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class RegistryHelpersImpl {
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build(null);
    }
}
