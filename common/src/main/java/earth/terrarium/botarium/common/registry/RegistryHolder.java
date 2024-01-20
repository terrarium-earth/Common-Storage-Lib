package earth.terrarium.botarium.common.registry;

import net.minecraft.core.Registry;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.function.Supplier;

public class RegistryHolder<V> {

    /**
     * @param registry The {@link Registry} that will be used to register the entries under.
     * @param modid    The id of the mod that all entries in this registry will get registered under.
     */
    @ImplementedByExtension
    public RegistryHolder(Registry<V> registry, String modid) {
        throw new NotImplementedException();
    }

    /**
     * Registers an entry to the registry.
     *
     * @param id       The id of the entry.
     * @param registry The {@link Supplier} that will be registered into the registry with the id and the mod id provided in the constructor.
     * @return An instance of a {@link Supplier} that holds the entry that was registered.
     */
    @ImplementedByExtension
    public <T extends V> Supplier<T> register(String id, Supplier<T> registry) {
        throw new NotImplementedException();
    }

    /**
     * @return A {@link Collection} of all the entries in this registry as suppliers.
     */
    @ImplementedByExtension
    public Collection<? extends Supplier<V>> getRegistries() {
        throw new NotImplementedException();
    }

    /**
     * Registers all entries in this registry. Should be called in your mod's initialization method.
     */
    @ImplementedByExtension
    public void initialize() {
    }
}
