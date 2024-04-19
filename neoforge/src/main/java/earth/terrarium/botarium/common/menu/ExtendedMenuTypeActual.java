package earth.terrarium.botarium.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.msrandom.multiplatform.annotations.Actual;

public interface ExtendedMenuTypeActual {
    @Actual
    static <T extends AbstractContainerMenu, D> MenuType<T> create(ExtendedMenuType.MenuFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec) {
        return new NeoMenuType<>(factory, codec, FeatureFlags.DEFAULT_FLAGS);
    }
}
