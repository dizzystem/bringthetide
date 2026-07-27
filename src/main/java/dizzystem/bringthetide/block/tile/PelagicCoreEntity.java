package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.recipe.PelagicRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class PelagicCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-2, 0, -1), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(-2, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(-1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(2, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.CUT_SANDSTONE)
    );

    public PelagicCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.PELAGIC_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //this is called every getTicksPerAction() ticks
    @Override
    public void doPeriodicAction(ServerLevel level, Vec3 pos){
        //run a recipe
        ArrayList<ItemStack> ingredients = this.getPoolCores().stream().map(
                corePos -> (level.getBlockEntity(corePos) instanceof PelagicCoreEntity pelagicCoreEntity)
                        ? pelagicCoreEntity.getItemStack()
                        : ItemStack.EMPTY
        ).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        if (ingredients.size() <= 0){
            return;
        }
        Collections.shuffle(ingredients);

        IItemHandlerModifiable inputs = new ItemStackHandler(ingredients.size());
        for (int i=0;i<ingredients.size();i++){
            inputs.setStackInSlot(i, ingredients.get(i));
        }
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        Optional<PelagicRecipe> maybeRecipe =
                level.getRecipeManager().getRecipeFor(
                        TideRecipes.PELAGIC.get(),
                        inputWrapper,
                        level);
        if (!maybeRecipe.isPresent()) {
            return;
        }

        PelagicRecipe recipe = maybeRecipe.get();
        ItemStack output = recipe.assemble(inputWrapper, level.registryAccess());
        for (var itemStack : ingredients){
            itemStack.split(1);
        }
        this.getPoolCores().forEach(corePos -> {
            //send client update
            if (level.getBlockEntity(corePos) instanceof PelagicCoreEntity){
                BlockState blockState = level.getBlockState(corePos);
                level.sendBlockUpdated(corePos, blockState, blockState, Block.UPDATE_CLIENTS);
            }
        });

        if (!(output.getItem() instanceof SpawnEggItem spawnEgg)){
            return;
        }

        EntityType<?> entitytype = spawnEgg.getType(output.getTag());
        entitytype.spawn(level, BlockPos.containing(pos), MobSpawnType.SPAWNER);

        level.sendParticles(TideParticles.SPLASH.get(),
                pos.x,
                pos.y + 1,
                pos.z,
                10,
                0,
                0,
                0,
                0.1);
    }
}
