package earth.terrarium.botarium.common.data.impl;

import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.utils.ComponentExtras;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public record DataManagerImpl<T>(AttachmentType<T> attachmentType,
                                 @Nullable DataComponentType<T> componentType) implements DataManager<T> {
    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrCreate(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(componentType, attachmentType.initializer().get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getDataOrThrow(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrThrow(this.attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return Optional.ofNullable(holder.get(componentType)).orElseThrow(() -> new RuntimeException(dataHolder + " is not initialized"));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getDataOrInit(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrSet(this.attachmentType, data);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(componentType, data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T setData(Object dataHolder, T data) {
        return switch (dataHolder) {
            case AttachmentTarget holder -> holder.setAttached(attachmentType, data);
            case ItemStack holder -> holder.set(Objects.requireNonNull(componentType()), data);
            case ComponentExtras holder -> holder.setComponent(Objects.requireNonNull(componentType()), data);
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public T removeData(Object dataHolder) {
        return switch (dataHolder) {
            case AttachmentTarget holder -> holder.removeAttached(attachmentType);
            case ItemStack holder -> holder.remove(Objects.requireNonNull(componentType()));
            case ComponentExtras holder -> holder.removeComponent(Objects.requireNonNull(componentType()));
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.hasAttached(this.attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.has(componentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }
}
