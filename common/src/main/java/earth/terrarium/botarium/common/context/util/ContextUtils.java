package earth.terrarium.botarium.common.context.util;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;

public class ContextUtils {
    public static long exchangeAll(ItemContext context, ItemUnit unit, boolean simulate) {
        return context.exchange(unit, context.mainSlot().getAmount(), simulate);
    }
}
