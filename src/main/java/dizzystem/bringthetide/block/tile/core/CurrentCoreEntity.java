package dizzystem.bringthetide.block.tile.core;

import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.entity.RitualTnt;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Map;

import static java.util.Map.entry;

public class CurrentCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE)
    );

    public CurrentCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.CURRENT_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    @Override
    public void entityInPool(Entity entity, Level level, BlockPos pos, RitualTnt tnt){
        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        BlockPos above = getBlockPos().relative(Direction.UP);
        BlockEntity entityAbove = level.getBlockEntity(above);
        if (entityAbove == null) {
            return;
        }
        LazyOptional<IItemHandler> itemHandler = entityAbove.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN);
        if (!itemHandler.isPresent()) {
            return;
        }

        ItemStack insert = itemEntity.getItem();
        ItemStack remainder = ItemHandlerHelper.insertItem(itemHandler.orElse(null), insert, false);
        if (insert.equals(remainder)){
            //didn't do anything
            return;
        }

        ((ServerLevel) level).sendParticles(TideParticles.BUBBLE.get(),
                pos.getX() + 0.5,
                getBlockPos().getY() + 1,
                pos.getZ() + 0.5,
                4,
                0,
                0,
                0,
                0.2);
        itemEntity.setItem(remainder);
    }
}
