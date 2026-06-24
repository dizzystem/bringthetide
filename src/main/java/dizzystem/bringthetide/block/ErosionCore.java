package dizzystem.bringthetide.block;

import dizzystem.bringthetide.tile.DepositionCoreEntity;
import dizzystem.bringthetide.tile.ErosionCoreEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.ParametersAreNonnullByDefault;

public class ErosionCore extends Core {
    public ErosionCore() {
        super(Properties.of().strength(3.5F));
    }

    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState){
        return new ErosionCoreEntity(blockPos, blockState);
    }

    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ErosionCoreEntity be){
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult blockHitResult){
        if (!(level.getBlockEntity(pos) instanceof ErosionCoreEntity core)){
            return InteractionResult.PASS;
        }

        //right clicking fluids in or out
        ItemStack item = player.getItemInHand(hand);
        if (!FluidUtil.getFluidHandler(item).isPresent() ||
            !core.getFluidHandler().isPresent()) {
            return InteractionResult.PASS;
        }
        FluidStack coreFluid = core.getFluid();
        IFluidHandler coreFluidHandler = core.getFluidHandler().orElse(null);
        FluidActionResult result;
        if (!coreFluid.isEmpty()){ //take out the fluid
            result = FluidUtil.tryFillContainer(item, coreFluidHandler, coreFluid.getAmount(),
                    player, true);
            if (result.isSuccess()){
                if (item.getCount() > 1){
                    item.shrink(1);
                    player.getInventory().placeItemBackInInventory(result.result);
                } else {
                    player.setItemInHand(hand, result.result);
                }
                core.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, core.getBlockPos());
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        result = FluidUtil.tryEmptyContainer(item, coreFluidHandler, ErosionCoreEntity.TANK_CAPACITY,
                player, true);
        if (result.isSuccess()){ //put in fluid
            if (item.getCount() > 1){
                item.shrink(1);
                player.getInventory().placeItemBackInInventory(result.result);
            } else {
                player.setItemInHand(hand, result.result);
            }
            core.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, core.getBlockPos());
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
