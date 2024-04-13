package earth.terrarium.botarium.common.data.impl;

import earth.terrarium.botarium.common.data.DataManager;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public record DataManagerImpl<T>(Supplier<AttachmentType<T>> attachmentType,
                                 Supplier<DataComponentType<T>> componentType) implements DataManager<T> {

    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.get(componentType.get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getDataOrThrow(Object dataHolder) {
        if (!hasData(dataHolder)) {
            throw new RuntimeException(dataHolder + "is not initialized");
        } else {
            return getData(dataHolder);
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
            case ItemStack holder -> holder.set(componentType.get(), data);
            case FluidStack holder -> holder.set(componentType.get(), data);
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.hasData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.has(componentType.get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public @Nullable DataComponentType<T> componentType() {
        return componentType.get();
    }
}
