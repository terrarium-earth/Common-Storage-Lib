package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.DataManagerBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class DataManagerBuilderImpl<T> implements DataManagerBuilder<T> {
    private final DeferredRegister<AttachmentType<?>> registry;
    private final DeferredRegister<DataComponentType<?>> componentRegistry;
    private final AttachmentType.Builder<T> builder;

    private Codec<T> codec;
    private StreamCodec<? super RegistryFriendlyByteBuf, T> clientCodec;
    private boolean registerComponentType;

    public DataManagerBuilderImpl(DeferredRegister<AttachmentType<?>> registry, DeferredRegister<DataComponentType<?>> componentRegistry, Supplier<T> factory) {
        this.registry = registry;
        this.componentRegistry = componentRegistry;
        this.builder = AttachmentType.builder(factory);
    }

    @Override
    public DataManagerBuilder<T> serialize(Codec<T> codec) {
        builder.serialize(codec);
        this.codec = codec;
        return this;
    }

    @Override
    public DataManagerBuilder<T> copyOnDeath() {
        builder.copyOnDeath();
        return this;
    }

    @Override
    public DataManagerBuilder<T> syncToClient(StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        this.clientCodec = codec;
        return this;
    }

    @Override
    public DataManagerBuilder<T> withDataComponent() {
        this.registerComponentType = true;
        return this;
    }

    @Override
    public DataManager<T> buildAndRegister(String name) {
        var type = registry.register(name, builder::build);
        Supplier<DataComponentType<T>> component = null;
        if (registerComponentType) {
            DataComponentType.Builder<T> componentBuilder = DataComponentType.<T>builder().persistent(codec).networkSynchronized(clientCodec);
            component = componentRegistry.register(name, componentBuilder::build);
        }
        return new DataManagerImpl<>(type, component);
    }
}