package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.menu.ExtraDataMenuProvider;
import earth.terrarium.botarium.api.menu.MenuHooks;
import earth.terrarium.botarium.fabric.menu.ExtraDataMenuProviderWrapper;
import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions;
import net.fabricmc.fabric.impl.screenhandler.Networking;
import net.minecraft.server.level.ServerPlayer;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.ParametersAreNonnullByDefault;

@ClassExtension(MenuHooks.class)
@ParametersAreNonnullByDefault
public class MenuHooksExtensions {

    @ImplementsBaseElement
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        player.openMenu(new ExtraDataMenuProviderWrapper(provider));
    }
}
