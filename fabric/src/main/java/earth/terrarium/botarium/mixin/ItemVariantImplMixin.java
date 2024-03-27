package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.fabric.data.DataUtils;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.impl.attachment.AttachmentSerializingImpl;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemVariantImpl.class)
public class ItemVariantImplMixin implements AttachmentTargetImpl {

    @Inject(method = "toNbt()Lnet/minecraft/nbt/CompoundTag;", at = @At("TAIL"))
    public void $botarium_writeAttachments(CallbackInfoReturnable<CompoundTag> cir) {
        fabric_writeAttachmentsToNbt(cir.getReturnValue());
    }

    @Inject(method = "fromNbt(Lnet/minecraft/nbt/CompoundTag;)Lnet/fabricmc/fabric/api/transfer/v1/item/ItemVariant;", at = @At("RETURN"))
    private static void $botarium_readAttachments(CompoundTag tag, CallbackInfoReturnable<ItemVariant> cir) {
        if (!cir.getReturnValue().isBlank()) {
            ((AttachmentTargetImpl) cir.getReturnValue()).fabric_readAttachmentsFromNbt(tag);
        }
    }
}
