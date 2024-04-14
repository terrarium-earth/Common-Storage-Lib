package earth.terrarium.botarium.common.menu;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuHooksActual {
    @Actual
    public static <T> void openMenu(ServerPlayer player, ExtraDataMenuProvider<T> provider) {
        player.openMenu(new ExtraDataMenuProviderWrapper<>(provider));
    }

    public record ExtraDataMenuProviderWrapper<T>(ExtraDataMenuProvider<T> providerWrapper) implements ExtendedScreenHandlerFactory<T> {

        @Override
        public @NotNull Component getDisplayName() {
            return providerWrapper.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
            return providerWrapper.createMenu(i, inventory, player);
        }

        @Override
        public T getScreenOpeningData(ServerPlayer player) {
            return providerWrapper.getExtraData(player);
        }
    }
}
