package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.fabric.data.DataUtils;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemVariant.class)
public interface ItemVariantMixin {

    @Inject(method = "of(Lnet/minecraft/world/item/ItemStack;)Lnet/fabricmc/fabric/api/transfer/v1/item/ItemVariant;", at = @At("RETURN"))
    private static void $botarium_copyDataFromStack(ItemStack stack, CallbackInfoReturnable<ItemVariant> cir) {
        DataUtils.copyAttachments((AttachmentTargetImpl) (Object) stack, (AttachmentTargetImpl) cir.getReturnValue(), (ignored) -> false);
    }

    @Inject(method = "matches(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    default void $botarium_doAttachmentsMatch(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && DataUtils.areAttachmentsCompatible((AttachmentTargetImpl) (Object) stack, (AttachmentTargetImpl) this));
    }

    @Inject(method = "toStack(I)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    default void $botarium_copyAttachmentsOntoStack(int count, CallbackInfoReturnable<ItemStack> cir) {
        DataUtils.copyAttachments((AttachmentTargetImpl) this, (AttachmentTargetImpl) (Object) cir.getReturnValue(), (ignored) -> false);
    }
}
