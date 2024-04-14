package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class FluidSounds {
    private final Map<String, SoundEvent> sounds = new HashMap<>();

    public void addSound(String name, SoundEvent sound) {
        sounds.put(name, sound);
    }

    public SoundEvent getSound(String name) {
        return sounds.get(name);
    }

    public Map<String, SoundEvent> getSounds() {
        return sounds;
    }
}
