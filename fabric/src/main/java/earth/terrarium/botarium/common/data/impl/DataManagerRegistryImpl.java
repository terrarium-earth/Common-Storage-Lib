package earth.terrarium.botarium.common.data.impl;

import earth.terrarium.botarium.common.data.DataManagerBuilder;
import earth.terrarium.botarium.common.data.DataManagerRegistry;

import java.util.function.Supplier;

public record DataManagerRegistryImpl(String modid) implements DataManagerRegistry {

    @Override
    public <T> DataManagerBuilder<T> builder(Supplier<T> factory) {
        return new DataManagerBuilderImpl<>(modid, factory);
    }

    @Override
    public void initialize() {

    }
}
