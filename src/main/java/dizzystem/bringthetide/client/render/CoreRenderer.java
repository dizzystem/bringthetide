package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.tile.CoreEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Objects;

public class CoreRenderer implements BlockEntityRenderer<CoreEntity> {
    private final TextureAtlasSprite overlaySprite;

    public CoreRenderer(BlockEntityRendererProvider.Context context){
        this.overlaySprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                TextureAtlas.LOCATION_BLOCKS
                ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/overlay_cross")));
    }

    /**
     * copied rendering code from <a href="https://github.com/VazkiiMods/Botania/blob/1.20.x/Xplat/src/main/java/vazkii/botania/client/core/helper/RenderHelper.java#L469">Botania</a>
     *
     * @param startX   Start x position in blocks
     * @param startY   Start position in blocks
     * @param endX     End x position in blocks
     * @param endY     End y position in blocks
     *
     * @param uvStartX UV start x position in "pixels" (1/16th sprite size)
     * @param uvStartY UV start position in "pixels" (1/16th sprite size)
     * @param uvEndX   UV end x position in "pixels" (1/16th sprite size)
     * @param uvEndY   UV end y position in "pixels" (1/16th sprite size)
     */
    public static void renderIconFullBright(
            PoseStack ms, VertexConsumer buffer,
            float startX, float startY, float endX, float endY,
            int uvStartX, int uvStartY, int uvEndX, int uvEndY,
            TextureAtlasSprite icon, int color, float alpha, int light) {
        Matrix4f mat = ms.last().pose();
        Matrix3f normal = ms.last().normal();
        float red = ((color >> 16) & 0xFF) / 255F;
        float green = ((color >> 8) & 0xFF) / 255F;
        float blue = (color & 0xFF) / 255F;

        buffer.vertex(mat, startX, endY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvStartX), icon.getV(uvEndY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, endX, endY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvEndX), icon.getV(uvEndY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, endX, startY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvEndX), icon.getV(uvStartY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, startX, startY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvStartX), icon.getV(uvStartY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
    }

    @Override
    public void render(CoreEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        BlockPos entityPos = entity.getBlockPos();
        long millis = System.currentTimeMillis();
        float alpha = 0.5f + (float) Math.cos((float) (millis % 4000) * Math.PI*2f / 4000f) / 2f;

        //only one of getMissingBlocks or getPoolBlocks should have any entries
        //if there are missing blocks, render translucent blocks in those positions
        for (var entry : entity.getMissingBlocksAllowed().entrySet()){
            BlockPos blockPos = entry.getKey();
            BlockState[] allowedBlocks = entry.getValue();

            if (allowedBlocks == null){
                //This shouldn't happen
                LogUtils.getLogger().info("allowedBlocks is null at core pos {}", blockPos);
                //for (var entry : entity.getMissingBlocksAllowed().entrySet()){
                //    LogUtils.getLogger().info("{} : {}", entry.getKey(), entry.getValue());
                //}
            } else {
                BlockState allowedBlock = allowedBlocks[(int)((millis % (1000 * allowedBlocks.length)) / 1000)];

                VertexConsumer consumer = bufferSource.getBuffer(ModRenderTypes.GHOST);
                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                ModelBlockRenderer modelRenderer = dispatcher.getModelRenderer();
                BakedModel model = dispatcher.getBlockModel(allowedBlock);

                poseStack.pushPose();
                poseStack.translate(blockPos.getX() - entityPos.getX(), blockPos.getY() - entityPos.getY(),
                        blockPos.getZ() - entityPos.getZ());
                //prevent z fighting if there's another block there
                poseStack.translate(-0.001, -0.001,-0.001);
                poseStack.scale(1.002f, 1.002f, 1.002f);

                modelRenderer.renderModel(poseStack.last(), consumer, allowedBlock, model, 1, 1, 1, LightTexture.FULL_BRIGHT, combinedOverlay);
                //renderer.renderSingleBlock(allowedBlock, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.WHITE_OVERLAY_V,
                //        net.minecraftforge.client.model.data.ModelData.EMPTY, RenderType.translucent());
                poseStack.popPose();
            }

        }

        //if we have poolBlocks that means the pool is valid, render a flat texture over our poolBlocks
        for (var blockPos : entity.getPoolBlocks()){
            poseStack.pushPose();

            //horizontal
            poseStack.mulPose(new Quaternionf().rotateX((float) Math.PI/2));
            //raise it a tiny bit so it renders above the block surface
            poseStack.translate(0f, 0f, -1.001f);
            //offset it to the block
            poseStack.translate(blockPos.getX() - entityPos.getX(), blockPos.getZ() - entityPos.getZ(),
                    0f);

            VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
            renderIconFullBright(poseStack, builder,
                    0, 0, 1,1,
                    0, 0, 16, 16,
                    this.overlaySprite, 0x33FFCC,
                    alpha, LightTexture.FULL_BRIGHT);
            poseStack.popPose();
        }
    }
}
