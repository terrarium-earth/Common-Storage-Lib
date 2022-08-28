package testmod;

import net.minecraftforge.fml.common.Mod;

@Mod(TestMod.MODID)
public class ForgeTestMod {
    public ForgeTestMod() {
        TestMod.init();
    }
}
