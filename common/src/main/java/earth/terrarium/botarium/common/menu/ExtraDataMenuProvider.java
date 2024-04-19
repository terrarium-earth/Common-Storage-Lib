package earth.terrarium.botarium.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public interface ExtraDataMenuProvider<T> extends MenuProvider {
    T getExtraData(ServerPlayer player);

    @Nullable
    @Override
    AbstractContainerMenu createMenu(int i, Inventory inventory, Player player);
}
