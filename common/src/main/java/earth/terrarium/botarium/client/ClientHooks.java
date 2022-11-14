package earth.terrarium.botarium.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class ClientHooks {

    @ImplementedByExtension
    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static <T extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static void setRenderLayer(Block block, RenderType type) {
        throw new NotImplementedException();
    }
}
