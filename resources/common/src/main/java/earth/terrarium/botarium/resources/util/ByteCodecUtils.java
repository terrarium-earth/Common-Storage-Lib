package earth.terrarium.botarium.resources.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class ByteCodecUtils {
    private static final Gson GSON = new Gson();

    public static final ByteCodec<ResourceLocation> RESOURCE_LOCATION = ByteCodec.STRING.map(ResourceLocation::new, ResourceLocation::toString);


    public static <T> ByteCodec<T> fromCodec(Codec<T> codec) {
        return new ByteCodec<T>() {
            @Override
            public void encode(T value, ByteBuf buffer) {
                DataResult<JsonElement> dataResult = codec.encodeStart(JsonOps.INSTANCE, value);
                ByteCodec.STRING.encode(GSON.toJson(Util.getOrThrow(dataResult, (string) -> new EncoderException("Failed to encode: " + string + " " + value))), buffer);
            }

            @Override
            public T decode(ByteBuf buffer) {
                JsonElement jsonElement = GsonHelper.fromJson(GSON, ByteCodec.STRING.decode(buffer), JsonElement.class);
                DataResult<T> dataResult = codec.parse(JsonOps.INSTANCE, jsonElement);
                return Util.getOrThrow(dataResult, (string) -> new DecoderException("Failed to decode json: " + string));
            }
        };
    }
}
