package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.registry.RegistryHolder;
import net.minecraft.core.Registry;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

@ClassExtension(RegistryHolder.class)
public class ForgeRegistryHelper<V> {

    @ExtensionInjectedElement
    private final DeferredRegister<V> register;

    @ImplementsBaseElement
    public ForgeRegistryHelper(Registry<V> registry, String modid) {
        this.register = DeferredRegister.create(registry.key(), modid);
    }

    @ImplementsBaseElement
    public <T extends V> Supplier<T> register(String id, Supplier<T> object) {
        return this.register.register(id, object);
    }

    @ImplementsBaseElement
    public Collection<? extends Supplier<V>> getRegistries() {
        return register.getEntries().stream().map(holder -> (Supplier<V>) holder::get).toList();
    }

    @ImplementsBaseElement
    public void initialize() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.register.register(modEventBus);
    }

}
