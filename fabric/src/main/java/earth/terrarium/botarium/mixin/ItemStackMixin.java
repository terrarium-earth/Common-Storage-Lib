package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.fabric.data.DataUtils;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentEntrypoint;
import net.fabricmc.fabric.impl.attachment.AttachmentSerializingImpl;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ItemStack.class)
public class ItemStackMixin implements AttachmentTargetImpl {
    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "TAIL"))
    public void $botarium_initAttachments(CompoundTag compoundTag, CallbackInfo ci) {
        fabric_readAttachmentsFromNbt(compoundTag);
    }

    @Inject(method = "save(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At(value = "TAIL"))
    public void $botarium_saveAttachments(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        fabric_writeAttachmentsToNbt(compoundTag);
    }

    @Inject(method = "copy", at = @At("RETURN"))
    public void $botarium_copyAttachments(CallbackInfoReturnable<ItemStack> cir) {
        DataUtils.copyAttachments(this, (AttachmentTargetImpl) (Object) cir.getReturnValue(), (ignored) -> true);
    }

    @Inject(method = "isSameItemSameTags(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private static void $botarium_areAttachmentsEqual(ItemStack stack, ItemStack other, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && DataUtils.areAttachmentsCompatible((AttachmentTargetImpl) (Object) stack, (AttachmentTargetImpl) (Object) other));
    }
}
