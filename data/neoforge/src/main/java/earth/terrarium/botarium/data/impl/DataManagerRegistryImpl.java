package earth.terrarium.botarium.data.impl;

import earth.terrarium.botarium.data.DataManagerBuilder;
import earth.terrarium.botarium.data.DataManagerRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public record DataManagerRegistryImpl(DeferredRegister<AttachmentType<?>> register, DeferredRegister<DataComponentType<?>> componentRegister) implements DataManagerRegistry {
    public DataManagerRegistryImpl(String modid) {
        this(DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, modid), DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, modid));
    }

    @Override
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(register, componentRegister, factory);
    }

    @Override
    public void initialize() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        this.register.register(modEventBus);
        this.componentRegister.register(modEventBus);
    }
}
