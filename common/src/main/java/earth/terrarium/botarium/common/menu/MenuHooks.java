package earth.terrarium.botarium.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.msrandom.multiplatform.annotations.Expect;

public class MenuHooks {
    @Expect
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider);
}
