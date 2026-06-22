package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
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
        Vector3f spiral;
        if (entity.craftingEntity != null && entity.maxCraftingTimer > 0){
            if (entity.craftingTimer <= 0){
                //craft should be done already, we're probably just lagging
                return;
            }
            BlockPos itemPos = entity.craftingEntity.blockPosition();
            BlockPos corePos = entity.getBlockPos();

            Vector3f coreToItem = new Vector3f(
                    itemPos.getX() - corePos.getX(),
                    itemPos.getY() - corePos.getY(),
                    itemPos.getZ() - corePos.getZ());
            Vector3f ItemToCore = new Vector3f(
                    corePos.getX() - itemPos.getX(),
                    corePos.getY() - itemPos.getY(),
                    corePos.getZ() - itemPos.getZ());
            //convert to polar coords
            double r = Math.hypot(ItemToCore.x, ItemToCore.z);
            double theta = Math.atan2(ItemToCore.z, ItemToCore.x);
            double y = ItemToCore.y;

            double timeLeft = Math.max(0, (double) (entity.craftingTimer + partialTicks) / entity.maxCraftingTimer);
            //move closer to centre over time
            //for the first 3/4, start fast and slow down
            //at the 3/4 mark we should be at exactly at 1
            //for the last 1/4, start slow and speed up
            r = timeLeft > 0.25 ?
                    1 + (r - 1) * Math.pow(timeLeft - 0.25, 2) :
                    2 * Math.pow(timeLeft, 0.5);
            //spin around over time
            theta += Math.PI * 3 * (1 - timeLeft);
            //for the first 3/4, rise up slow
            //at the 3/4 mark we should be at exactly at 1
            //for the last 1/4, go back down fast
            y = timeLeft > 0.25 ?
                    0.75 - 4.0/3 * Math.pow(timeLeft - 0.25, 2) :
                    -0.75 + 1.5 * Math.pow(4 * timeLeft, 0.5);

            spiral = coreToItem.add(new Vector3f(
                    (float) (r * Math.cos(theta)),
                    (float) y,
                    (float) (r * Math.sin(theta))));
        } else {
            spiral = new Vector3f(0, 0, 0);
        }

        poseStack.pushPose();
        //if crafting, spiral in towards the item
        poseStack.translate(spiral.x, spiral.y, spiral.z);
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
        //move a tiny bit closer to the player so we render on top of the item
        Vec3 spritePos = entity.getBlockPos().getCenter();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()
                .add(0, -1, 0); //camera position is 1 off
        Vec3 towardPlayer = cameraPos.subtract(spritePos).normalize();
        poseStack.translate(
                0.25 * towardPlayer.x,
                0.25 * towardPlayer.y,
                0.25 * towardPlayer.z);
        //if crafting, spiral in towards the item
        poseStack.translate(spiral.x, spiral.y, spiral.z);
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
