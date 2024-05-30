package earth.terrarium.botarium.data;

import earth.terrarium.botarium.data.impl.DataManagerBuilderImpl;
import earth.terrarium.botarium.data.sync.DataSyncSerializer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Actual
public final class DataManagerRegistryActual {
    private final DeferredRegister<AttachmentType<?>> register;
    private final DeferredRegister<DataComponentType<?>> componentRegister;
    private final DeferredRegister<DataSyncSerializer<?>> serializerRegister;

    @Actual
    private DataManagerRegistryActual(String modid) {
        register = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, modid);
        componentRegister = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, modid);
        serializerRegister = DeferredRegister.create(BotariumData.SYNC_SERIALIZERS, modid);
    }

    @Actual
    public static DataManagerRegistry create(String modid) {
        return new DataManagerRegistry(modid);
    }

    @Actual
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(register, componentRegister, serializerRegister, factory);
    }

    @Actual
    public void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.register.register(modEventBus);
        this.componentRegister.register(modEventBus);
    }
}