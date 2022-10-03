package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.registry.RegistryHolder;
import net.minecraft.core.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Supplier;

@ClassExtension(RegistryHolder.class)
@ParametersAreNonnullByDefault
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
        return register.getEntries();
    }

    @ImplementsBaseElement
    public void initialize() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.register.register(modEventBus);
    }
}
