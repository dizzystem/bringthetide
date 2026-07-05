package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.tile.CoreEntity;
import dizzystem.bringthetide.tile.FluidCoreEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FluidCoreRenderer extends CoreRenderer {
    private final TextureAtlasSprite bubbleSprite;
    private final Map<FluidStack, TextureAtlasSprite> fluidTextureCache = new HashMap<>();
    private final Map<FluidStack, Integer> fluidTintCache = new HashMap<>();

    public static final float FLUID_SCALE = 0.9f;

    public FluidCoreRenderer(BlockEntityRendererProvider.Context context){
        super(context);

        this.bubbleSprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                TextureAtlas.LOCATION_BLOCKS
        ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/large_thick_bubble")));
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

        Quaternionf cameraRotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        float bob = (float) Math.cos((2000 - (millis % 4000)) * Math.PI*2f / 2000f) * 0.2f;
        int fullness = (int) Math.ceil(16f * fluidStack.getAmount() / FluidCoreEntity.TANK_CAPACITY);

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        //float above the core
        //bob up and down
        poseStack.translate(1f, 2.8f + bob, 1f);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the bubble
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-0.5f * FLUID_SCALE, -0.5f * FLUID_SCALE, 0f);

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
            renderIconFullBright(poseStack, builder,
                    0,  FLUID_SCALE * fullness / 16, FLUID_SCALE, FLUID_SCALE,
                    fullness, 16, 16, 16,
                    fluidTextureCache.get(fluidStack), fluidTintCache.get(fluidStack),
                    1f, LightTexture.FULL_BRIGHT);
        } else {
            renderIconFullBright(poseStack, builder,
                    0,  0, FLUID_SCALE, FLUID_SCALE * fullness / 16,
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
        poseStack.scale(0.5f, 0.5f, 0.5f);
        //float above the core at the same position as the fluid
        //bob up and down
        poseStack.translate(1f, 2.8f + bob, 1f);
        //face the player
        poseStack.mulPose(cameraRotation);
        //align the centre of our sprite to the centre of the fluid
        //this needs to happen after rotation so that the rotation happens around the same point for both renders
        poseStack.translate(-0.89f, -0.89f, 0f);

        builder = bufferSource.getBuffer(RenderType.cutout());
        renderIconFullBright(poseStack, builder,
                0, 0, 1.6f, 1.6f,
                0, 0, 19, 19,
                this.bubbleSprite, 0xFFFFFF,
                1f, LightTexture.FULL_BRIGHT);
        poseStack.popPose();

        if (entity.craftingEntity != null && entity.maxCraftingTimer > 0) {
            if (entity.craftingTimer <= 0) {
                //craft should be done already, we're probably just lagging
                return;
            }
            BlockPos itemPos = entity.craftingEntity.blockPosition();
            BlockPos corePos = entity.getBlockPos();


        }
    }
}
