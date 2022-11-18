package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.BotariumCap;
import earth.terrarium.botarium.forge.BotariumItemCapabilityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;
import net.msrandom.extensions.annotations.ClassExtension;
import org.jetbrains.annotations.Nullable;

@ClassExtension(BotariumCap.class)
public interface BotariumCapImpl extends IForgeItem {

    @Override
    default @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new BotariumItemCapabilityProvider(stack, nbt);
    }

    @Override
    @Nullable
    default CompoundTag getShareTag(ItemStack stack) {
        CompoundTag shareTag = IForgeItem.super.getShareTag(stack);
        if (shareTag == null) {
            shareTag = new CompoundTag();
        }
        shareTag.put("ForgeCaps", stack.save(new CompoundTag()).getCompound("ForgeCaps"));
        return shareTag;
    }

    @Override
    default void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        if(nbt != null) {
            stack.deserializeNBT(nbt);
        }
    }
}
