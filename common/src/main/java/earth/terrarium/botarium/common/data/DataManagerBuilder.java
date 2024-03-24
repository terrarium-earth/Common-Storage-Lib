package earth.terrarium.botarium.common.data;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface DataManagerBuilder<T> {
    DataManagerBuilder<T> serialize(Codec<T> codec);
    DataManagerBuilder<T> copyOnDeath();
    DataManager<T> buildAndRegister(String name);
}
