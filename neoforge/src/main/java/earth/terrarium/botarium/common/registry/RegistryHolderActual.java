package earth.terrarium.botarium.common.registry;

import net.minecraft.core.Registry;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.Collection;
import java.util.function.Supplier;

@Actual
public class RegistryHolderActual<V> {
    @Actual
    public RegistryHolderActual(Registry<V> registry, String modid) {
    }

    @Actual
    public <T extends V> Supplier<T> register(String id, Supplier<T> registry) {
        return null;
    }

    @Actual
    public Collection<? extends Supplier<V>> getRegistries() {
        return null;
    }

    @Actual
    public void initialize() {
    }
}
