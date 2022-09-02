package earth.terrarium.botarium.api.menu;

import net.minecraft.server.level.ServerPlayer;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class MenuHooks {

    @ImplementedByExtension
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        throw new NotImplementedException();
    }
}