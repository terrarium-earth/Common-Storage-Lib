package earth.terrarium.botarium.fabric.fluid.holder;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class MutableItemVariant implements ItemVariant {
    private final ItemStack stack;

    public MutableItemVariant(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean isBlank() {
        return stack.isEmpty();
    }

    @Override
    public Item getObject() {
        return stack.getItem();
    }

    @Override
    public @Nullable CompoundTag getNbt() {
        return stack.getTag();
    }

    @Override
    public CompoundTag toNbt() {
        return stack.save(new CompoundTag());
    }

    @Override
    public void toPacket(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }
}
