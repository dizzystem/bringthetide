package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.DepositionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.ParametersAreNonnullByDefault;

public class DepositionCore extends Core {
    public DepositionCore() {
        super(BlockBehaviour.Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new DepositionCoreEntity(blockPos, blockState);
    }

    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof DepositionCoreEntity be){
                if (lvl.isClientSide()){
                    be.tickClient();
                } else {
                    be.tickServer();
                }
            }
        };
    }

    @ParametersAreNonnullByDefault
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result){
        if (!(level.getBlockEntity(pos) instanceof DepositionCoreEntity core)){
            return InteractionResult.PASS;
        }

        //right clicking items in or out
        ItemStack playerStack = player.getItemInHand(hand);
        ItemStack coreStack = core.getItemStack();
        if (!coreStack.isEmpty()){ //take out the item
            ItemStack copy = coreStack.copy();
            player.getInventory().placeItemBackInInventory(copy);
            core.setItemStack(ItemStack.EMPTY);
            core.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, core.getBlockPos());
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else if (!playerStack.isEmpty()){ //put in an item
            core.setItemStack(playerStack.split(1));
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos blockPos, BlockState newState, boolean pistonMoved){
        //drop our inv on the ground when broken
        if (!newState.is(oldState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof DepositionCoreEntity depositionCoreEntity){
                ItemStack item = depositionCoreEntity.getItemStack();
                Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), item);
            }
        }

        super.onRemove(oldState, level, blockPos, newState, pistonMoved);
    }
}
