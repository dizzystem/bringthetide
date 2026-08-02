package dizzystem.bringthetide.block;

import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.util.FluidHandler;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public class Core extends Block implements EntityBlock {
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final BiFunction<BlockPos, BlockState, ? extends CoreEntity> coreEntityConstructor;

    public Core(Properties properties, BiFunction<BlockPos, BlockState, ? extends CoreEntity> coreEntityConstructor){
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(HORIZONTAL_FACING, Direction.NORTH));
        this.coreEntityConstructor = coreEntityConstructor;
    }

    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof CoreEntity be){
                if (lvl.isClientSide()){
                    be.tickClient();
                } else {
                    be.tickServer();
                }
            }
        };
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HORIZONTAL_FACING);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos blockPos, BlockState newState, boolean pistonMoved){
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (blockEntity instanceof CoreEntity coreEntity){
            coreEntity.clearImbuedWater();
            PoolHandler.deleteCore(level, blockPos);
        }

        //drop our inv on the ground when broken
        if (!newState.is(oldState.getBlock())){
            LazyOptional<IItemHandler> OItemHandler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (OItemHandler.isPresent()){
                IItemHandler itemHandler = OItemHandler.orElse(null);
                for (int i=0;i<itemHandler.getSlots();i++){
                    Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                            itemHandler.getStackInSlot(i));
                }
            }
        }

        super.onRemove(oldState, level, blockPos, newState, pistonMoved);
    }

    @ParametersAreNonnullByDefault
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult blockHitResult){
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null){
            return InteractionResult.PASS;
        }

        //right clicking fluids in or out
        boolean rightClickFluid = FluidHandler.tryRightClickFluidIntoTank(player, hand, be);
        if (rightClickFluid){
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        //right clicking items in or out
        LazyOptional<IItemHandler> OItemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (OItemHandler.isPresent()){
            IItemHandler itemHandler = OItemHandler.orElse(null);

            ItemStack playerStack = player.getItemInHand(hand);
            ItemStack coreStack = itemHandler.getStackInSlot(0);
            if (!coreStack.isEmpty()){ //take out the item
                ItemStack extracted = itemHandler.extractItem(0, 1, false);
                player.getInventory().placeItemBackInInventory(extracted);
                be.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, be.getBlockPos());
                return InteractionResult.sidedSuccess(level.isClientSide());
            } else if (!playerStack.isEmpty()){ //put in an item
                ItemStack remainder = ItemHandlerHelper.insertItem(itemHandler, playerStack, false);
                if (remainder != playerStack){
                    player.setItemInHand(hand, remainder);
                    be.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, be.getBlockPos());
                    return InteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemstack){
        if (!(entity instanceof Player player)){
            return;
        }

        //pass on who placed us to our block entity
        if (level.getBlockEntity(blockPos) instanceof CoreEntity coreEntity){
            coreEntity.setPlacedBy(player.getUUID());
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return this.coreEntityConstructor.apply(blockPos, blockState);
    }
}
