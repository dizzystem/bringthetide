package dizzystem.bringthetide.util;

import dizzystem.bringthetide.block.tile.ErosionCoreEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidHandler {
    public static boolean tryRightClickFluidIntoTank(Player player, InteractionHand hand, BlockEntity be){
        //right clicking fluids in or out
        LazyOptional<IFluidHandler> OFluidHandler = be.getCapability(ForgeCapabilities.FLUID_HANDLER);
        ItemStack item = player.getItemInHand(hand);
        if (OFluidHandler.isPresent() && FluidUtil.getFluidHandler(item).isPresent()){
            IFluidHandler fluidHandler = OFluidHandler.orElse(null);
            FluidStack coreFluid = fluidHandler.getFluidInTank(0);
            FluidActionResult result;
            if (!coreFluid.isEmpty()){ //take out the fluid
                result = FluidUtil.tryFillContainer(item, fluidHandler, coreFluid.getAmount(),
                        player, true);
                if (result.isSuccess()){
                    if (!player.isCreative()) {
                        if (item.getCount() > 1) {
                            item.shrink(1);
                            player.getInventory().placeItemBackInInventory(result.result);
                        } else {
                            player.setItemInHand(hand, result.result);
                        }
                    }
                    be.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, be.getBlockPos());
                    return true;
                }
            }

            result = FluidUtil.tryEmptyContainer(item, fluidHandler, ErosionCoreEntity.TANK_CAPACITY,
                    player, true);
            if (result.isSuccess()){ //put in fluid
                if (!player.isCreative()) {
                    if (item.getCount() > 1) {
                        item.shrink(1);
                        player.getInventory().placeItemBackInInventory(result.result);
                    } else {
                        player.setItemInHand(hand, result.result);
                    }
                }
                be.getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, be.getBlockPos());
                return true;
            }
        }

        return false;
    }
}
