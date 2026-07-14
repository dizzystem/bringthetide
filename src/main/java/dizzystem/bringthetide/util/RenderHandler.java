package dizzystem.bringthetide.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dizzystem.bringthetide.BringTheTide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RenderHandler {
    public static final TextureAtlasSprite itemBubbleSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
            TextureAtlas.LOCATION_BLOCKS
    ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/large_bubble")));;
    public static final TextureAtlasSprite fluidBubbleSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
            TextureAtlas.LOCATION_BLOCKS
    ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/large_thick_bubble")));;
    public static final Map<FluidStack, TextureAtlasSprite> fluidTextureCache = new HashMap<>();
    public static final Map<FluidStack, Integer> fluidTintCache = new HashMap<>();

    /**
     * from <a href="https://stackoverflow.com/questions/3684269/component-of-a-quaternion-rotation-around-an-axis">StackOverflow</a>
     * Decompose the rotation into 2 parts.
     * @param twist rotation around the "direction" vector
     * @param swing rotation around axis that is perpendicular to "direction" vector
     */
    public static void SwingTwistDecomposition(Quaternionf rotation, Vector3f direction, Quaternionf swing,
                                               Quaternionf twist){
        Vector3f rotationAxis = new Vector3f(rotation.x, rotation.y, rotation.z);
        Vector3f projection = direction.mul(rotationAxis.dot(direction));
        twist.set(projection.x, projection.y, projection.z, rotation.w);
        swing.set(new Quaternionf(rotation).mul(twist.conjugate()));
    }

    /**
     * from <a href="https://github.com/VazkiiMods/Botania/blob/1.20.x/Xplat/src/main/java/vazkii/botania/client/core/helper/RenderHelper.java#L469">Botania</a>
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

        buffer.vertex(mat, endX, startY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvStartX), icon.getV(uvEndY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, startX, startY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvEndX), icon.getV(uvEndY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, startX, endY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvEndX), icon.getV(uvStartY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
        buffer.vertex(mat, endX, endY, 0).color(red, green, blue, alpha)
                .uv(icon.getU(uvStartX), icon.getV(uvStartY)).overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light).normal(normal, 0, 0, 1).endVertex();
    }

    public static void renderItemBubble(BlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource,
                                        Vec3 offset, float scale, ItemStack itemStack, int combinedOverlay) {
        long millis = System.currentTimeMillis();
        Quaternionf cameraRotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        Quaternionf timeRotation = Axis.YP.rotationDegrees((float)(millis % 4000) * 360f / 4000f);

        poseStack.pushPose();
        //float above the core
        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.scale(scale, scale, scale);
        //slowly rotate
        poseStack.mulPose(timeRotation);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, combinedOverlay,
                poseStack, bufferSource, Minecraft.getInstance().level, 0);
        poseStack.popPose();

        poseStack.pushPose();
        //move a tiny bit closer to the player so we render on top of the item
        Vec3 spritePos = entity.getBlockPos().getCenter();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()
                .add(0, -1, 0); //camera position is 1 off
        Vec3 towardPlayer = cameraPos.subtract(spritePos).normalize();
        poseStack.translate(
                0.25 * towardPlayer.x,
                0.25 * towardPlayer.y,
                0.25 * towardPlayer.z);
        //float above the core at the same position as the item
        poseStack.translate(offset.x, offset.y, offset.z);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the item
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-scale, -scale, 0f);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        RenderHandler.renderIconFullBright(poseStack, builder,
                0, 0, 1.79f * scale, 1.79f * scale,
                0, 0, 19, 19,
                itemBubbleSprite, 0xFFFFFF,
                1f, LightTexture.FULL_BRIGHT);
        poseStack.popPose();

    }

    public static void renderFluidBubble(BlockEntity entity, PoseStack poseStack, MultiBufferSource bufferSource,
                                         Vec3 offset, float scale, FluidStack fluidStack, int fullness) {
        Quaternionf cameraRotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();

        poseStack.pushPose();
        //float above the core
        poseStack.translate(offset.x, offset.y, offset.z);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the bubble
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-0.5f * scale, -0.5f * scale, 0f);

        if (!fluidTextureCache.containsKey(fluidStack)){
            ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluidStack.getFluid())
                    .getStillTexture(fluidStack);
            TextureAtlasSprite sprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                    TextureAtlas.LOCATION_BLOCKS
            ).apply(stillTexture));
            fluidTextureCache.put(fluidStack, sprite);
            int tint = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
            fluidTintCache.put(fluidStack, tint);
        }

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        if (fluidStack.getFluid().getFluidType().isLighterThanAir()){
            RenderHandler.renderIconFullBright(poseStack, builder,
                    0,  0, scale * fullness / 16, scale,
                    fullness, 16, 16, 16,
                    fluidTextureCache.get(fluidStack), fluidTintCache.get(fluidStack),
                    1f, LightTexture.FULL_BRIGHT);
        } else {
            RenderHandler.renderIconFullBright(poseStack, builder,
                    0,  0, scale, scale * fullness / 16,
                    0, 16 - fullness, 16, 16,
                    fluidTextureCache.get(fluidStack), fluidTintCache.get(fluidStack),
                    1f, LightTexture.FULL_BRIGHT);
        }
        poseStack.popPose();

        poseStack.pushPose();
        //move a tiny bit closer to the player so we render on top of the fluid
        Vec3 spritePos = entity.getBlockPos().getCenter();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()
                .add(0, -1, 0); //camera position is 1 off
        Vec3 towardPlayer = cameraPos.subtract(spritePos).normalize().scale(0.01);
        poseStack.translate(
                towardPlayer.x,
                towardPlayer.y,
                towardPlayer.z);
        //float above the core at the same position as the fluid
        poseStack.translate(offset.x, offset.y, offset.z);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the fluid
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-scale, -scale, 0f);

        builder = bufferSource.getBuffer(RenderType.cutout());
        RenderHandler.renderIconFullBright(poseStack, builder,
                0, 0, 1.79f * scale, 1.79f * scale,
                0, 0, 19, 19,
                fluidBubbleSprite, 0xFFFFFF,
                1f, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }
}
