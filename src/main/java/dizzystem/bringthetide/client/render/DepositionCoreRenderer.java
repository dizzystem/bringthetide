package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.tile.CoreEntity;
import dizzystem.bringthetide.tile.DepositionCoreEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class DepositionCoreRenderer extends CoreRenderer {
    private final TextureAtlasSprite bubbleSprite;

    public DepositionCoreRenderer(BlockEntityRendererProvider.Context context){
        super(context);

        this.bubbleSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                TextureAtlas.LOCATION_BLOCKS
        ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/large_bubble")));
    }

    @Override
    public void render(CoreEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        long millis = System.currentTimeMillis();
        super.render(entity, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);

        ItemStack itemStack = ((DepositionCoreEntity) entity).getItemStack();
        if (itemStack.isEmpty()){
            return;
        }

        Quaternionf cameraRotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        Quaternionf timeRotation = Axis.YP.rotationDegrees((float)(millis % 4000) * 360f / 4000f);
        float bob = (float) Math.cos((2000 - (millis % 4000)) * Math.PI*2f / 2000f) * 0.2f;

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        //float above the core
        poseStack.translate(1f, 2.8f + bob, 1f);
        //slowly rotate
        poseStack.mulPose(timeRotation);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, combinedOverlay,
                poseStack, bufferSource, Minecraft.getInstance().level, 0);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        //float above the core at the same position as the item
        //bob up and down
        poseStack.translate(1f, 2.8f + bob, 1f);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the item
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-0.96f, -0.96f, 0f);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        renderIconFullBright(poseStack, builder,
                0, 0, 1.6f,1.6f,
                0, 0, 20, 20,
                this.bubbleSprite, 0xFFFFFF,
                1f, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }
}
