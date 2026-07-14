package dizzystem.bringthetide.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.util.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class CoreRenderer implements BlockEntityRenderer<CoreEntity> {
    private final TextureAtlasSprite[] overlaySprites;
    private final int NUM_DIRS = 4;

    public CoreRenderer(BlockEntityRendererProvider.Context context){
        this.overlaySprites = new TextureAtlasSprite[] {
                getTextureSprite("block/overlay_circle_0"),
                getTextureSprite("block/overlay_circle_1"),
                getTextureSprite("block/overlay_circle_straight"),
                getTextureSprite("block/overlay_circle_elbow"),
                getTextureSprite("block/overlay_circle_3"),
                getTextureSprite("block/overlay_circle_4"),
        };
    }

    private TextureAtlasSprite getTextureSprite(String path){
        return Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
                TextureAtlas.LOCATION_BLOCKS
        ).apply(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, path)));
    }

    /**
     * Returns an integer representing the overlay sprite and rotation of that sprite that the
     *  given pool block should have rendered on it.
     *
     * @param blockPos the pool block
     * @param poolBlocks the other blocks in the same pool
     */
    public int getOverlayType(BlockPos blockPos, HashSet<BlockPos> poolBlocks){
        Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        ArrayList<Direction> neighbours = new ArrayList<>();
        int overlayType;

        for (int i=0;i<dirs.length;i++){
            Direction dir = dirs[i];

            if (poolBlocks.contains(blockPos.relative(dir))){
                neighbours.add(dir);
            }
        }

        if (neighbours.size() == 2){
            //either a straight or an elbow
            if (neighbours.get(0).equals(neighbours.get(1).getOpposite())){
                overlayType = NUM_DIRS * 2;
            } else {
                overlayType = NUM_DIRS * 3;
            }

            for (int i=0;i<dirs.length;i++){
                if (neighbours.get(0) == dirs[i]){
                    if (neighbours.get(1) == dirs[(i + NUM_DIRS - 1) % NUM_DIRS]){
                        //northwest special case
                        overlayType += (i + NUM_DIRS - 1) % NUM_DIRS;
                    } else {
                        overlayType += i;
                    }
                    break;
                }
            }

        } else if (neighbours.size() == 1){
            overlayType = NUM_DIRS;

            for (int i=0;i<dirs.length;i++){
                if (neighbours.contains(dirs[i])){
                    overlayType += i;
                    break;
                }
            }
        } else if (neighbours.size() == 3){
            overlayType = NUM_DIRS * 4;

            for (int i=0;i<dirs.length;i++){
                if (!neighbours.contains(dirs[i])){
                    overlayType += i;
                    break;
                }
            }
        } else if (neighbours.size() == 4){
            overlayType = NUM_DIRS * 5;
        } else {
            overlayType = 0;
        }

        return overlayType;
    }

    @Override
    public void render(CoreEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int combinedLight, int combinedOverlay){
        BlockPos entityPos = entity.getBlockPos();
        long millis = System.currentTimeMillis();

        //only one of getMissingBlocks or getPoolBlocks should have any entries
        //if there are missing blocks, render translucent blocks in those positions
        for (var entry : entity.getMissingBlocksAllowed().entrySet()){
            BlockPos blockPos = entry.getKey();
            BlockState[] allowedBlocks = entry.getValue();

            if (allowedBlocks == null){
                //This shouldn't happen
                LogUtils.getLogger().info("allowedBlocks is null at core pos {}", blockPos);
            } else {
                BlockState allowedBlock = allowedBlocks[(int)((millis % (1000 * allowedBlocks.length)) / 1000)];

                VertexConsumer consumer = bufferSource.getBuffer(TideRenderTypes.GHOST);
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
                poseStack.popPose();
            }

        }

        //if we have poolBlocks that means the pool is valid, render a flat texture over our poolBlocks
        float alpha = 0.5f + (float) Math.cos((float) (millis % 4000) * Math.PI*2f / 4000f) * 0.4f;
        if (!entity.getPoolCores().isEmpty()){
            //only the first core in the pool should do this
            if (!entity.getPoolCores().get(0).equals(entity.getBlockPos())){
                return;
            }
        }

        Map<BlockPos, Integer> renderOverlayData = entity.getRenderOverlayData();

        HashSet<BlockPos> poolBlocks = entity.getPoolBlocks();
        for (var blockPos : poolBlocks){
            int color;
            if (entity.poolFilled){
                color = 0x33FFCC; //todo: make it fade between colours maybe
            } else {
                color = 0xFF3366;
            }

            //cache the overlay data
            if (!renderOverlayData.containsKey(blockPos)){
                renderOverlayData.put(blockPos, getOverlayType(blockPos, poolBlocks));
            }
            Integer renderOverlayType = renderOverlayData.get(blockPos);
            TextureAtlasSprite sprite = this.overlaySprites[(int) renderOverlayType / NUM_DIRS];
            float rotation = (float) (Math.PI * (renderOverlayType % NUM_DIRS) / 2);

            //recolour some of the overlay to represent how much thalassity is left
            if (!entity.poolFilled && entity.renderThalassity < 1 && entity.getPoolCentre() != null){
                Vec3 vector = blockPos.getCenter().subtract(entity.getPoolCentre().getCenter());
                double angle = Math.PI + Math.atan2(vector.z, vector.x);
                if (entity.renderThalassity * Math.PI * 2 < angle){
                    color = 0x666666;
                }
            }

            poseStack.pushPose();

            //horizontal
            poseStack.mulPose(new Quaternionf().rotateX((float) Math.PI/2));
            //raise it a tiny bit so it renders above the block surface
            poseStack.translate(0f, 0f, -1.001f);
            //offset it to the block
            poseStack.translate(blockPos.getX() - entityPos.getX(), blockPos.getZ() - entityPos.getZ(),
                    0f);
            //rotate the sprite
            poseStack.translate(0.5f, 0.5f, 0);
            poseStack.mulPose(new Quaternionf().rotateZ(rotation));
            poseStack.translate(-0.5f, -0.5f, 0);

            VertexConsumer builder = bufferSource.getBuffer(RenderType.translucent());
            RenderHandler.renderIconFullBright(poseStack, builder,
                    0, 0, 1,1,
                    0, 0, 16, 16,
                    sprite, color,
                    alpha, LightTexture.FULL_BRIGHT);
            poseStack.popPose();
        }
    }
}
