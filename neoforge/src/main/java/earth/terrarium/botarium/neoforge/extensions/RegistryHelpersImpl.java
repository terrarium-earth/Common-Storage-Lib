package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.registry.RegistryHelpers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

@ClassExtension(RegistryHelpers.class)
public class RegistryHelpersImpl {

    @ImplementsBaseElement
    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(RegistryHelpers.MenuFactory<T> factory) {
        return IMenuTypeExtension.create(factory::create);
    }

    @ImplementsBaseElement
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }
}
