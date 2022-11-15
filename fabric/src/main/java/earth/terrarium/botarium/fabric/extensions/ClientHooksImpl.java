package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.client.ClientHooks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.function.Supplier;

@ClassExtension(ClientHooks.class)
public class ClientHooksImpl {
    @ImplementsBaseElement
    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        ItemProperties.register(pItem, pName, pProperty);
    }

    @ImplementsBaseElement
    public static <T extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRendererRegistry.register(type, provider);
    }

    @ImplementsBaseElement
    public static <T extends Entity> void registerEntityRenderer(Supplier<? extends EntityType<? extends T>> entity, EntityRendererProvider<T> rendererProvider) {
        EntityRendererRegistry.register(entity.get(), rendererProvider);
    }

    @ImplementsBaseElement
    public static void setRenderLayer(Block block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }
}
