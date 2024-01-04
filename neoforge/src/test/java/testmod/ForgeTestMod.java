package testmod;

import net.neoforged.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class ForgeTestMod {
    public ForgeTestMod() {
        TestMod.init();
    }
}
