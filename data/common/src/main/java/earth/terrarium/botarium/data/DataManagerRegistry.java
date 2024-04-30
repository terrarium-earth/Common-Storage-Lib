package earth.terrarium.botarium.data;

import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public interface DataManagerRegistry {
    @Expect
    static DataManagerRegistry create(String modid);

    <T> DataManagerBuilder<T> builder(Supplier<T> factory);

    void initialize();
}
