package earth.terrarium.botarium.data;

import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

@Expect
public final class DataManagerRegistry {
    public static DataManagerRegistry create(String modid);

    private DataManagerRegistry(String modid);

    public <T> DataManagerBuilder<T> builder(Supplier<T> factory);

    public void initialize();
}
