package earth.terrarium.common_storage_lib.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface DataManagerBuilder<T> {
    DataManagerBuilder<T> copyOnDeath();
    DataManagerBuilder<T> serialize(Codec<T> codec);
    DataManagerBuilder<T> networkSerializer(StreamCodec<? super RegistryFriendlyByteBuf, T> codec);
    DataManagerBuilder<T> networkSerializer();

    DataManagerBuilder<T> withDataComponent();

    DataManager<T> buildAndRegister(String name);
}
