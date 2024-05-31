package earth.terrarium.common_storage_lib.item.input;

import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.resources.util.ByteCodecUtils;
import net.minecraft.resources.ResourceLocation;

public record ConsumerType<T extends ItemConsumer>(ResourceLocation id, MapCodec<T> codec, ByteCodec<T> byteCodec) {
    public ConsumerType(ResourceLocation location, MapCodec<T> codec) {
        this(location, codec, ByteCodecUtils.fromCodec(codec.codec()));
    }
}
