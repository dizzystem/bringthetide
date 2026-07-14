package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.block.tile.FluidCoreEntity;
import dizzystem.bringthetide.util.RenderHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

public class FluidCoreRenderer extends CoreRenderer {
    public FluidCoreRenderer(BlockEntityRendererProvider.Context context){
        super(context);
    }

    @Override
    public void render(CoreEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        long millis = System.currentTimeMillis();
        super.render(entity, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);

        FluidStack fluidStack = ((FluidCoreEntity) entity).getFluid();
        if (fluidStack.isEmpty()){
            return;
        }

        float bob = (float) Math.cos((2000 - (millis % 4000)) * Math.PI*2f / 2000f) * 0.1f;
        int fullness = (int) Math.ceil(16f * fluidStack.getAmount() / FluidCoreEntity.TANK_CAPACITY);

        RenderHandler.renderFluidBubble(entity, poseStack, bufferSource,
                new Vec3(0.5f, 1.4f + bob, 0.5f), 0.5f, fluidStack, fullness);
    }
}
