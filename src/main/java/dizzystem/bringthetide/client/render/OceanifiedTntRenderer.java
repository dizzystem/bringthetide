package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dizzystem.bringthetide.entity.OceanifiedTnt;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;

//copied from net/minecraft/client/renderer/entity/TntRenderer.java
public class OceanifiedTntRenderer extends EntityRenderer<OceanifiedTnt>  {
    private final BlockRenderDispatcher blockRenderer;

    public OceanifiedTntRenderer(EntityRendererProvider.Context ctx){
        super(ctx);
        this.shadowRadius = 0.5F;
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }

    public void render(OceanifiedTnt tntEntity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        int i = tntEntity.getFuse();
        if ((float)i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float f1 = 1.0F + f * 0.3F;
            poseStack.scale(f1, f1, f1);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, TideBlocks.OCEANIFIED_TNT.get().defaultBlockState(),
                poseStack, bufferSource, packedLight, i / 5 % 2 == 0);
        poseStack.popPose();
        super.render(tntEntity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    public ResourceLocation getTextureLocation(OceanifiedTnt tntEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
