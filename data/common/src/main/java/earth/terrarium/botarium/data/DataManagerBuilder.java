package earth.terrarium.botarium.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface DataManagerBuilder<T> {
    DataManagerBuilder<T> copyOnDeath();
    DataManagerBuilder<T> serialize(Codec<T> codec);
    DataManagerBuilder<T> networkSerializer(StreamCodec<? super RegistryFriendlyByteBuf, T> codec);

    DataManagerBuilder<T> withDataComponent();
    DataManagerBuilder<T> autoSync();

    DataManager<T> buildAndRegister(String name);
}
