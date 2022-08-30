package earth.terrarium.botarium.api.registry;

import net.minecraft.core.Registry;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.function.Supplier;

public class RegistryHolder<V> {
    @ImplementedByExtension
    public RegistryHolder(Registry<V> registry, String modid) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public <T extends V> Supplier<T> register(String id, Supplier<T> registry) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public Collection<? extends Supplier<V>> getRegistries() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public void initialize() {
    }
}
