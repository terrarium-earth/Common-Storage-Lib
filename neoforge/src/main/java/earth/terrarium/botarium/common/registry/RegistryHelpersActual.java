package earth.terrarium.botarium.common.registry;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public class RegistryHelpersActual {
    @Actual
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

    @Actual
    public static <T extends AbstractContainerMenu, D> MenuType<T> createMenuType(RegistryHelpers.MenuFactory<T, D> factory, StreamCodec<RegistryFriendlyByteBuf, D> codec) {
        return IMenuTypeExtension.create((syncId, inventory, byteBuf) -> factory.create(syncId, inventory, codec.decode(byteBuf)));
    }
}
