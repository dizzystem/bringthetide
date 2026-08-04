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
}
