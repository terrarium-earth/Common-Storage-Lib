package earth.terrarium.botarium.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.NonnullDefault;

@NonnullDefault
public class NeoMenuType<T extends AbstractContainerMenu, D> extends MenuType<T> implements ExtendedMenuType<T, D> {
    private final StreamCodec<? super RegistryFriendlyByteBuf, D> codec;
    private final ExtendedMenuType.MenuFactory<T, D> factory;

    public NeoMenuType(ExtendedMenuType.MenuFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> codec,FeatureFlagSet requiredFeatures) {
        //noinspection DataFlowIssue
        super(null, requiredFeatures);
        this.codec = codec;
        this.factory = factory;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, D> getCodec() {
        return codec;
    }

    @Override
    public @NotNull T create(int windowId, Inventory playerInv, RegistryFriendlyByteBuf extraData) {
        return create(windowId, playerInv, getCodec().decode(extraData));
    }

    @Override
    public T create(int syncId, Inventory inventory, D extraData) {
        return factory.create(syncId, inventory, extraData);
    }
}
