package earth.terrarium.botarium.data.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import earth.terrarium.botarium.data.sync.AutoSyncRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundBlockEntityDataPacket.class)
public class ClientboundBlockEntityDataPacketMixin {

    /*
    @ModifyExpressionValue(method = "create(Lnet/minecraft/world/level/block/entity/BlockEntity;Ljava/util/function/BiFunction;)Lnet/minecraft/network/protocol/game/ClientboundBlockEntityDataPacket;", at = @At(value = "INVOKE", target = "Ljava/util/function/BiFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static <R> R $botarium_data$injectSyncData(R original, @Local(argsOnly = true) BlockEntity blockEntity) {
        CompoundTag tag = (CompoundTag) original;
        AutoSyncRegistry.fromTargetToTag(blockEntity);
        return (R) tag;
    }
     */
}
