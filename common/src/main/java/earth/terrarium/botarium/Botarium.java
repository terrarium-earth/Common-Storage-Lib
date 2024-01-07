package earth.terrarium.botarium;

import com.mojang.datafixers.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Botarium {
    public static final String MOD_ID = "botarium";
    public static final String BOTARIUM_DATA = "BotariumData";
    public static final Logger LOGGER = LogManager.getLogger("Botarium");

    public static void init() {

    }

    public static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized, String type) {
        if (finalized == null) {
            Botarium.LOGGER.debug("Finalizing {} registration", type);
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

}