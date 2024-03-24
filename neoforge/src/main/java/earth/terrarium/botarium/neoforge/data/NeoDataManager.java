package earth.terrarium.botarium.neoforge.data;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.common.data.DataManager;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;

import javax.naming.OperationNotSupportedException;
import java.util.function.Supplier;

public class NeoDataManager<T> implements DataManager<T> {
    private final Supplier<AttachmentType<T>> attachmentType;
    public NeoDataManager(Supplier<AttachmentType<T>> attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Override
    public T getData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getData(attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T setData(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.setData(attachmentType, data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public boolean hasData(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.hasData(attachmentType);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }
}
