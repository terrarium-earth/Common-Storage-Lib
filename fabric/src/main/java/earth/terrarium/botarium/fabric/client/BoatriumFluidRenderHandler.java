package earth.terrarium.botarium.fabric.client;

import earth.terrarium.botarium.common.registry.fluid.FluidInformation;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class BoatriumFluidRenderHandler implements FluidRenderHandler {

    protected final FluidInformation information;
    protected TextureAtlasSprite[] sprites;

    public BoatriumFluidRenderHandler(FluidInformation information) {
        this.information = information;
        this.sprites = new TextureAtlasSprite[2];
    }

    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return this.sprites;
    }

    @Override
    public void reloadTextures(TextureAtlas textureAtlas) {
        if (information.overlay() != null && sprites.length < 3) {
            sprites = new TextureAtlasSprite[3];
        } else if (information.overlay() == null && sprites.length > 2) {
            sprites = new TextureAtlasSprite[2];
        }
        sprites[0] = textureAtlas.getSprite(information.still());
        sprites[1] = textureAtlas.getSprite(information.flowing());

        if (information.overlay() != null) {
            sprites[2] = textureAtlas.getSprite(information.overlay());
        }
    }

    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return information.tintColor();
    }
}
