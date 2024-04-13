package earth.terrarium.botarium.common.data;

import com.mojang.serialization.Codec;
import net.msrandom.multiplatform.annotations.Expect;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface DataManagerRegistry {

    @Expect
    static DataManagerRegistry create(String modid) {
        throw new NotImplementedException("DataManagerRegistry.create is not implemented");
    }

    default <T> DataManager<T> register(@NotNull String name, @NotNull Supplier<T> factory, @Nullable Codec<T> codec, boolean copyOnDeath) {
        DataManagerBuilder<T> builder = builder(factory);

        if (codec != null) {
            builder.serialize(codec);
        }
        if (copyOnDeath) {
            builder.copyOnDeath();
        }

        return builder.buildAndRegister(name);
    }

    <T> DataManagerBuilder<T> builder(Supplier<T> factory);

    void initialize();
}
