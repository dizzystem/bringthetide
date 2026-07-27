package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.entity.OceanifiedTnt;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class WhirlpoolCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-2, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(-2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, -1), Blocks.PRISMARINE)
    );

    public WhirlpoolCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.WHIRLPOOL_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    @Override
    public void entityInPool(Entity entity, Level level, BlockPos pos, OceanifiedTnt tnt) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        //figure out where we're going
        ArrayList<ItemStack> locators = this.getPoolCores().stream().map(
                corePos -> (level.getBlockEntity(corePos) instanceof WhirlpoolCoreEntity whirlpoolCoreEntity)
                        ? whirlpoolCoreEntity.getItemStack()
                        : ItemStack.EMPTY
        ).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        if (locators.size() <= 0){
            return;
        }
    }
}
