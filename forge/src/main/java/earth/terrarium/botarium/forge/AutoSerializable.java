package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.api.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AutoSerializable extends INBTSerializable<CompoundTag> {
    Serializable getSerializable();

    @Override default CompoundTag serializeNBT() {
        return getSerializable().serialize(new CompoundTag());
    }

    @Override default void deserializeNBT(CompoundTag arg) {
        getSerializable().deserialize(arg);
    }
}
