package earth.terrarium.botarium.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.msrandom.multiplatform.annotations.Actual;

public class MenuHooksActual {
    @Actual
    public static <T> void openMenu(ServerPlayer player, ExtraDataMenuProvider<T> provider) {
        player.openMenu(provider, (data) -> provider.getExtraData(player));
    }
}
