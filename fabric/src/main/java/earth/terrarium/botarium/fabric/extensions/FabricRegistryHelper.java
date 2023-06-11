package earth.terrarium.botarium.fabric.extensions;

import com.google.common.base.Suppliers;
import earth.terrarium.botarium.common.registry.RegistryHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@ClassExtension(RegistryHolder.class)
public class FabricRegistryHelper<V> {
    @ExtensionInjectedElement
    private final Registry<V> registry;

    @ExtensionInjectedElement
    private final String modid;

    @ExtensionInjectedElement
    private final List<Supplier<V>> entries = new ArrayList<>();

    @ImplementsBaseElement
    public FabricRegistryHelper(Registry<V> registry, String modid) {
        this.registry = registry;
        this.modid = modid;
    }

    @ImplementsBaseElement
    public <T extends V> Supplier<T> register(String id, Supplier<T> object) {
        var register = Registry.register(registry, new ResourceLocation(modid, id), object.get());
        entries.add(Suppliers.memoize(() -> register));
        return () -> register;
    }

    @ImplementsBaseElement
    public Collection<? extends Supplier<V>> getRegistries() {
        return entries;
    }

    @ImplementsBaseElement
    public void initialize() {
        //Its empty because i love. also because fabric registers when register is called. and also because i love <3
    }
}
