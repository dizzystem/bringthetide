package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static java.util.Map.entry;

public class SinkholeCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -3), Blocks.SANDSTONE)
    );
    private final List<ItemStack> tools = List.of(
            new ItemStack(Items.IRON_PICKAXE),
            new ItemStack(Items.IRON_AXE),
            new ItemStack(Items.IRON_SHOVEL),
            new ItemStack(Items.IRON_HOE),
            new ItemStack(Items.IRON_SWORD)
    );

    public SinkholeCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.SINKHOLE_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    public void tickServer(){
        super.tickServer();

        if (!this.isPoolActive()){
            return;
        }

        BlockPos poolCentre = this.getPoolCentre();

        //every 4 seconds
        if (ticks % getTicksPerAction() == 0){
            ServerLevel level = (ServerLevel) getLevel();
            int minY = level.getMinBuildHeight() + 1;
            int maxY = getBlockPos().getY() - 1;
            RandomSource random = level.getRandom();
            BlockPos[] poolFluids = getPoolFluids().toArray(new BlockPos[getPoolFluids().size()]);

            int successes = 0;
            //try 64 times to find a mineable block, up to 4 total
            for (int i=0;i<64;i++){
                if (doQuarryAttempt(level, minY, maxY, random, poolFluids)){
                    successes++;
                }

                if (successes >= 4){
                    break;
                }
            }

            if (successes > 0){
                level.sendParticles(TideParticles.SPLASH.get(),
                        poolCentre.getX() + 0.5,
                        poolCentre.getY() + 1.5,
                        poolCentre.getZ() + 0.5,
                        10,
                        0,
                        0,
                        0,
                        0.1);
            }
        }
    }

    protected boolean doQuarryAttempt(ServerLevel level, int minY, int maxY, RandomSource random, BlockPos[] poolFluids){
        //pick a random block under a random pool fluid block
        BlockPos poolFluid = poolFluids[random.nextInt(0, poolFluids.length)];
        BlockPos targetPos = new BlockPos(poolFluid.getX(), random.nextInt(minY, maxY), poolFluid.getZ());
        BlockState targetState = level.getBlockState(targetPos);

        if (targetState.is(Blocks.AIR) || targetState.getFluidState() != Fluids.EMPTY.defaultFluidState()){
            return false;
        }

        ItemStack correctTool = null;
        for (var tool : this.tools){
            if (tool.isCorrectToolForDrops(targetState)){
                correctTool = tool;
                break;
            }
        }
        if (correctTool == null){
            return false;
        }

        List<ItemStack> drops = Block.getDrops(targetState, level, targetPos, level.getBlockEntity(targetPos));
        if (level.removeBlock(targetPos, false)){
            for (var item : drops){
                Vec3 poolCentre = this.getPoolCentre().getCenter();
                ItemEntity entity = new ItemEntity(level, poolCentre.x, poolCentre.y, poolCentre.z, item);
                level.addFreshEntity(entity);
            }
        }
        return true;
    }
}
