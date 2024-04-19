package earth.terrarium.botarium.common.registry;

import earth.terrarium.botarium.common.menu.ExtraDataMenu;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;

public class RegistryHelpersActual {
    @Actual
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelpers.BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
    }

    @Actual
    public static <T extends AbstractContainerMenu & ExtraDataMenu<?>, D> MenuType<T> createMenuType(RegistryHelpers.MenuFactory<T, D> factory) {
        return new ExtendedScreenHandlerType<>(factory::create, factory.getCodec);
    }
}
