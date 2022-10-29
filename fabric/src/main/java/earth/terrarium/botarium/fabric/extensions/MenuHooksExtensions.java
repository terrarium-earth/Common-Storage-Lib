package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.menu.ExtraDataMenuProvider;
import earth.terrarium.botarium.api.menu.MenuHooks;
import earth.terrarium.botarium.fabric.menu.ExtraDataMenuProviderWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(MenuHooks.class)
public class MenuHooksExtensions {

    @ImplementsBaseElement
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        player.openMenu(new ExtraDataMenuProviderWrapper(provider));
    }
}
