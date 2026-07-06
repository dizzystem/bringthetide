package dizzystem.bringthetide.fluid;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.registration.TideItems;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

//The base class is abstract because only either the Flowing or Source should ever be instantiated.
public abstract class FluidImbuedSeawater extends ForgeFlowingFluid {
    public int VANISH_CHANCE = 10;

    protected FluidImbuedSeawater(){
        super(new ForgeFlowingFluid.Properties(
                TideFluids.TYPE_IMBUED_SEAWATER,
                TideFluids.IMBUED_SEAWATER,
                TideFluids.FLOWING_IMBUED_SEAWATER
        ).block(TideBlocks.BLOCK_IMBUED_SEAWATER));
    }

    public static class Flowing extends FluidImbuedSeawater {
        public Flowing(){
            super();
            registerDefaultState(getStateDefinition().any().setValue(LEVEL,7));
        }

        protected void createFluidStateDefinition(@NotNull StateDefinition.Builder<Fluid, FluidState> stateDef){
            super.createFluidStateDefinition(stateDef);
            stateDef.add(LEVEL);
        }

        @Override
        public int getAmount(@NotNull FluidState state){
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState state){
            return false;
        }
    }

    public static class Source extends FluidImbuedSeawater {
        public Source(){
            super();
        }

        @Override
        public int getAmount(@NotNull FluidState state){
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState state){
            return true;
        }
    }

    public boolean hasSeawaterOnTwoOrMoreSides(BlockGetter getter, BlockPos pos){
        int num = 0;
        for (var horizontalDir : Direction.Plane.HORIZONTAL){
            BlockState horizontalState = getter.getBlockState(pos.relative(horizontalDir));
            if (horizontalState.is(TideBlocks.BLOCK_IMBUED_SEAWATER.get()) && horizontalState.getFluidState().isSource()){
                num ++;
            }
        }
        if (num >= 2){
            return true;
        }

        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean canSpreadTo(BlockGetter getter, BlockPos fromPos, BlockState fromState, Direction dir,
                                  BlockPos toPos, BlockState toState, FluidState fluidState, Fluid fluid){
        if (dir == Direction.DOWN){
            return false;
        }
        if (hasSeawaterOnTwoOrMoreSides(getter, toPos)){
            return true;
        }

        return super.canSpreadTo(getter, fromPos, fromState, dir, toPos, toState, fluidState, fluid);
    }

    //code from FlowingFluid.java
    //i need this but it's privated :(
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> OCCLUSION_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap = new Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>(200) {
            protected void rehash(int p_76102_) {
            }
        };
        object2bytelinkedopenhashmap.defaultReturnValue((byte)127);
        return object2bytelinkedopenhashmap;
    });

    private boolean canPassThroughWall(Direction p_76062_, BlockGetter p_76063_, BlockPos p_76064_, BlockState p_76065_, BlockPos p_76066_, BlockState p_76067_) {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> object2bytelinkedopenhashmap;

        if (p_76062_ == Direction.DOWN){
            return false;
        }

        if (!p_76065_.getBlock().hasDynamicShape() && !p_76067_.getBlock().hasDynamicShape()) {
            object2bytelinkedopenhashmap = OCCLUSION_CACHE.get();
        } else {
            object2bytelinkedopenhashmap = null;
        }

        Block.BlockStatePairKey block$blockstatepairkey;
        if (object2bytelinkedopenhashmap != null) {
            block$blockstatepairkey = new Block.BlockStatePairKey(p_76065_, p_76067_, p_76062_);
            byte b0 = object2bytelinkedopenhashmap.getAndMoveToFirst(block$blockstatepairkey);
            if (b0 != 127) {
                return b0 != 0;
            }
        } else {
            block$blockstatepairkey = null;
        }

        VoxelShape voxelshape1 = p_76065_.getCollisionShape(p_76063_, p_76064_);
        VoxelShape voxelshape = p_76067_.getCollisionShape(p_76063_, p_76066_);
        boolean flag = !Shapes.mergedFaceOccludes(voxelshape1, voxelshape, p_76062_);
        if (object2bytelinkedopenhashmap != null) {
            if (object2bytelinkedopenhashmap.size() == 200) {
                object2bytelinkedopenhashmap.removeLastByte();
            }

            object2bytelinkedopenhashmap.putAndMoveToFirst(block$blockstatepairkey, (byte)(flag ? 1 : 0));
        }

        return flag;
    }

    private boolean canHoldFluid(BlockGetter p_75973_, BlockPos p_75974_, BlockState p_75975_, Fluid p_75976_) {
        Block block = p_75975_.getBlock();
        if (block instanceof LiquidBlockContainer) {
            return ((LiquidBlockContainer)block).canPlaceLiquid(p_75973_, p_75974_, p_75975_, p_75976_);
        } else if (!(block instanceof DoorBlock) && !p_75975_.is(BlockTags.SIGNS) && !p_75975_.is(Blocks.LADDER) && !p_75975_.is(Blocks.SUGAR_CANE) && !p_75975_.is(Blocks.BUBBLE_COLUMN)) {
            if (!p_75975_.is(Blocks.NETHER_PORTAL) && !p_75975_.is(Blocks.END_PORTAL) && !p_75975_.is(Blocks.END_GATEWAY) && !p_75975_.is(Blocks.STRUCTURE_VOID)) {
                return !p_75975_.blocksMotion();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isSourceBlockOfThisType(FluidState p_76097_) {
        return p_76097_.getType().isSame(this) && p_76097_.isSource();
    }

    private boolean canPassThrough(BlockGetter p_75964_, Fluid p_75965_, BlockPos p_75966_, BlockState p_75967_, Direction p_75968_, BlockPos p_75969_, BlockState p_75970_, FluidState p_75971_) {
        return !this.isSourceBlockOfThisType(p_75971_) && this.canPassThroughWall(p_75968_, p_75964_, p_75966_, p_75967_, p_75969_, p_75970_) && this.canHoldFluid(p_75964_, p_75969_, p_75970_, p_75965_);
    }
    //end code from FlowingFluid.java

    @Override
    @ParametersAreNonnullByDefault
    protected Map<Direction, FluidState> getSpread(Level level, BlockPos blockPos, BlockState blockState){
        Map<Direction, FluidState> map = Maps.newEnumMap(Direction.class);

        for(Direction direction : Direction.Plane.HORIZONTAL){
            BlockPos blockpos = blockPos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            FluidState fluidstate = blockstate.getFluidState();
            FluidState fluidstate1 = this.getNewLiquid(level, blockpos, blockstate);
            if (this.canPassThrough(level, fluidstate1.getType(), blockPos, blockState, direction, blockpos, blockstate, fluidstate)){
                map.put(direction, fluidstate1);
            }
        }

        return map;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected int getSlopeDistance(LevelReader p_76027_, BlockPos p_76028_, int p_76029_, Direction p_76030_, BlockState p_76031_, BlockPos p_76032_, Short2ObjectMap<Pair<BlockState, FluidState>> p_76033_, Short2BooleanMap p_76034_) {
        return 1000;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(Level level, BlockPos blockPos, FluidState fluidState){
        super.tick(level, blockPos, fluidState);

        FluidState newfluidstate = this.getNewLiquid(level, blockPos, level.getBlockState(blockPos));
        int i = this.getSpreadDelay(level, blockPos, fluidState, newfluidstate);

        if (fluidState.isSource()){
            boolean ticked = false;
            for (var direction : Direction.values()){
                var adjPos = blockPos.relative(direction);
                var adjState = level.getBlockState(adjPos);

                if (adjState.is(TideTags.GROWTH_ACCELERATABLE) && adjState.isRandomlyTicking()){
                    //LogUtils.getLogger().info("Ticking block");
                    ticked = true;
                    adjState.randomTick((ServerLevel) level, adjPos, level.getRandom());
                    if (level.getRandom().nextInt(VANISH_CHANCE) == 0){
                        level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }

            if (ticked && !level.getBlockState(blockPos).isAir()){
                level.scheduleTick(blockPos, newfluidstate.getType(), i);
            }
        }
    }
}
