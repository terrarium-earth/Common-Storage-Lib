package earth.terrarium.botarium.common.registry;

import net.minecraft.core.Registry;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.Collection;
import java.util.function.Supplier;

@Expect
public class RegistryHolder<V> {

    /**
     * @param registry The {@link Registry} that will be used to register the entries under.
     * @param modid    The id of the mod that all entries in this registry will get registered under.
     */
    public RegistryHolder(Registry<V> registry, String modid);

    /**
     * Registers an entry to the registry.
     *
     * @param id       The id of the entry.
     * @param registry The {@link Supplier} that will be registered into the registry with the id and the mod id provided in the constructor.
     * @return An instance of a {@link Supplier} that holds the entry that was registered.
     */
    public <T extends V> Supplier<T> register(String id, Supplier<T> registry);

    /**
     * @return A {@link Collection} of all the entries in this registry as suppliers.
     */
    public Collection<? extends Supplier<V>> getRegistries();

    /**
     * Registers all entries in this registry. Should be called in your mod's initialization method.
     */
    public void initialize();
}
