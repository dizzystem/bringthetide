package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.block.tile.ItemCoreEntity;
import dizzystem.bringthetide.util.RenderHandler;
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

public class ItemCoreRenderer extends CoreRenderer {
    public ItemCoreRenderer(BlockEntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public void render(CoreEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        long millis = System.currentTimeMillis();
        super.render(entity, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);

        ItemStack itemStack = ((ItemCoreEntity) entity).getItemStack();
        if (itemStack.isEmpty()){
            return;
        }

        float bob = (float) Math.cos((2000 - (millis % 4000)) * Math.PI*2f / 2000f) * 0.1f;
        Vector3f spiral;
        if (entity.craftingEntity != null && !entity.craftingEntity.isRemoved() && entity.maxCraftingTimer > 0){
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

        RenderHandler.renderItemBubble(entity, poseStack, bufferSource,
                new Vec3(0.5f + spiral.x, 1.4f + bob + spiral.y, 0.5f + spiral.z),
                0.5f, itemStack, combinedOverlay);
    }
}
