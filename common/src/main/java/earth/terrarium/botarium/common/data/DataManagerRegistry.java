package earth.terrarium.botarium.common.data;

import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public interface DataManagerRegistry {

    @Expect
    static DataManagerRegistry create(String modid);

    <T> DataManagerBuilder<T> builder(Supplier<T> factory);

    void initialize();
}
