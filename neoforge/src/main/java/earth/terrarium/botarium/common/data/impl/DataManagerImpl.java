package earth.terrarium.botarium.common.data.impl;

import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.utils.ComponentExtras;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public record DataManagerImpl<T>(Supplier<AttachmentType<T>> attachmentType,
                                 @Nullable Supplier<DataComponentType<T>> component,
                                 Supplier<T> defaultValue) implements DataManager<T> {

    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(Objects.requireNonNull(componentType()), defaultValue.get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getDataOrThrow(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getExistingData(attachmentType).orElseThrow(() -> new NullPointerException(dataHolder + " is not initialized"));
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return Optional.ofNullable(holder.get(Objects.requireNonNull(componentType()))).orElseThrow(() -> new NullPointerException(dataHolder + " is not initialized"));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getDataOrInit(Object dataHolder, T data) {
        if (!hasData(dataHolder)) {
            setData(dataHolder, data);
        }
        return getData(dataHolder);
    }

    @Override
    public T setData(Object dataHolder, T data) {
        return switch (dataHolder) {
            case AttachmentHolder holder -> holder.setData(attachmentType, data);
            case ItemStack holder -> holder.set(Objects.requireNonNull(componentType()), data);
            case FluidStack holder -> holder.set(Objects.requireNonNull(componentType()), data);
            case ComponentExtras holder -> holder.setComponent(Objects.requireNonNull(componentType()), data);
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public T removeData(Object dataHolder) {
        return switch (dataHolder) {
            case AttachmentHolder holder -> holder.removeData(attachmentType);
            case ItemStack holder -> holder.remove(Objects.requireNonNull(componentType()));
            case FluidStack holder -> holder.remove(Objects.requireNonNull(componentType()));
            case ComponentExtras holder -> holder.removeComponent(Objects.requireNonNull(componentType()));
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.hasData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.has(Objects.requireNonNull(componentType()));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public DataComponentType<T> componentType() {
        return component == null ? null : component.get();
    }
}
