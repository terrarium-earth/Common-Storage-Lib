package earth.terrarium.botarium.common.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface DataManagerBuilder<T> {
    DataManagerBuilder<T> serialize(Codec<T> codec);
    DataManagerBuilder<T> copyOnDeath();

    DataManagerBuilder<T> syncToClient(StreamCodec<? super RegistryFriendlyByteBuf, T> codec);
    DataManagerBuilder<T> withDataComponent();

    DataManager<T> buildAndRegister(String name);

}
