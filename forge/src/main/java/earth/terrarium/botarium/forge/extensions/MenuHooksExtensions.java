package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.menu.ExtraDataMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import net.minecraft.server.level.ServerPlayer;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(MenuHooks.class)
public class MenuHooksExtensions {

    @ImplementsBaseElement
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        player.openMenu(provider, (data) -> provider.writeExtraData(player, data));
    }
}
