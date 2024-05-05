package earth.terrarium.botarium.data.mixin;

import earth.terrarium.botarium.data.network.BlockEntitySyncAllPacket;
import earth.terrarium.botarium.data.network.BlockEntitySyncPacket;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(method = "getUpdatePacket", at = @At("HEAD"))
    private void botarium_data$syncAllAttachments(CallbackInfoReturnable<Packet<ClientGamePacketListener>> cir) {
        PlayerLookup.tracking((BlockEntity) (Object) this).forEach(player -> {
            ServerPlayNetworking.send(player, BlockEntitySyncAllPacket.of((BlockEntity) (Object) this));
        });
    }
}
