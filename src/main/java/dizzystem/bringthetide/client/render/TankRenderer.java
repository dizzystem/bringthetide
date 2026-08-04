package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.tile.TankEntity;
import dizzystem.bringthetide.util.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TankRenderer implements BlockEntityRenderer<TankEntity> {
    private final TextureAtlasSprite bubbleSprite;
    private final Map<FluidStack, TextureAtlasSprite> fluidTextureCache = new HashMap<>();
    private final Map<FluidStack, Integer> fluidTintCache = new HashMap<>();

    public static final float FLUID_SCALE = 0.9f;

    public TankRenderer(BlockEntityRendererProvider.Context context){
        this.bubbleSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                TextureAtlas.LOCATION_BLOCKS
        ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/large_thick_bubble")));
    }

    @Override
    public void render(TankEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        FluidStack fluidStack = entity.getFluid();
        if (fluidStack.isEmpty()){
            return;
        }

        long millis = System.currentTimeMillis();
        int fullness = (int) Math.ceil(16f * fluidStack.getAmount() / TankEntity.TANK_CAPACITY);
        float bob = (float) Math.cos((2000 - (millis % 4000)) * Math.PI*2f / 2000f) * 0.025f;

        RenderHandler.renderFluidBubble(entity, poseStack, bufferSource,
                new Vec3(0.5f, 0.5f + bob, 0.5f + 0.0625), 0.6f, fluidStack, fullness);
    }
}
