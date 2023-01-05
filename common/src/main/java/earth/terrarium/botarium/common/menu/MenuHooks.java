package earth.terrarium.botarium.common.menu;

import net.minecraft.server.level.ServerPlayer;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class MenuHooks {

    /**
     * When called this will open the menu for the player and will call the
     * {@link ExtraDataMenuProvider#writeExtraData} method on the provider and
     * will send an open menu packet with the extra data.
     *
     * @param player The player to open the menu for.
     * @param provider The provider to create the menu from.
     */
    @ImplementedByExtension
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        throw new NotImplementedException();
    }
}