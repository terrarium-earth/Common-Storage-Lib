package earth.terrarium.botarium.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.msrandom.multiplatform.annotations.Expect;

public interface ExtendedMenuType<T extends AbstractContainerMenu, D> {
    @Expect
    static <T extends AbstractContainerMenu, D> MenuType<T> create(MenuFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec);

    StreamCodec<? super RegistryFriendlyByteBuf, D> getCodec();

    T create(int syncId, Inventory inventory, D extraData);

    interface MenuFactory<T extends AbstractContainerMenu, D> {
        T create(int syncId, Inventory inventory, D extraData);
    }
}
