package earth.terrarium.botarium.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface ExtraDataMenuProvider<T> extends MenuProvider {
    T getExtraData(ServerPlayer player);
}
