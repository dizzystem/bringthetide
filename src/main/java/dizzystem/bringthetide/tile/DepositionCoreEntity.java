package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.recipe.DepositionRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.registration.TideRecipes;
import dizzystem.bringthetide.util.OngoingCraft;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class DepositionCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, -1), Blocks.PRISMARINE)
    );

    public DepositionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.DEPOSITION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos){
        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        PoolHandler.attemptCraft(itemEntity, level, getBlockPos(), TideRecipes.DEPOSITION.get());
    }

    /* ===Crafting=== */
    @Override
    public void beginCraft(ItemEntity entity, ArrayList<BlockPos> cores) {
        //Check if we can craft anything with this entity and these cores.
        ItemStack mainIngredient = entity.getItem();
        ItemStack[] depositionCatalysts = cores.stream()
                .map(pos -> (level.getBlockEntity(pos) instanceof DepositionCoreEntity depositionCore) ?
                        depositionCore.getItemStack() : null)
                .filter(itemStack -> itemStack != null && !itemStack.isEmpty())
                .toArray(ItemStack[]::new);

        IItemHandlerModifiable inputs = new ItemStackHandler(depositionCatalysts.length + 1);
        inputs.setStackInSlot(0, mainIngredient);
        for (int i=0;i<depositionCatalysts.length;i++){
            inputs.setStackInSlot(i+1, depositionCatalysts[i]);
        }
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        Optional<DepositionRecipe> maybeRecipe =
                level.getRecipeManager().getRecipeFor(
                        TideRecipes.DEPOSITION.get(),
                        inputWrapper,
                        level);
        if (!maybeRecipe.isPresent()) {
            return;
        }

        //If so, add a timer to each of the cores involved in the recipe to start the crafting animation.
        ArrayList<BlockPos> involvedCores = cores.stream()
                .filter(pos -> (level.getBlockEntity(pos) instanceof DepositionCoreEntity depositionCore) &&
                        depositionCore.getItemStack() != null)
                .collect(Collectors.toCollection(ArrayList::new));

        DepositionRecipe recipe = maybeRecipe.get();
        involvedCores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof DepositionCoreEntity depositionCore){
                depositionCore.setMaxCraftingTimer(recipe.getCraftingTime() / getSpeed());
                depositionCore.setCraftingTimer(recipe.getCraftingTime() / getSpeed());
                depositionCore.setCraftingEntity(entity);
                BlockState blockState = level.getBlockState(pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
                ((ServerLevel) level).sendParticles(TideParticles.SPLASH.get(),
                        pos.getX() + .5,
                        pos.getY() + 1.5,
                        pos.getZ() + .5,
                        10,
                        0,
                        0,
                        0,
                        0.1);
            }
        });
        PoolHandler.addOngoingCraft(entity, new OngoingCraft(involvedCores, recipe));
        //Stop the item entity's horizontal momentum so it doesn't float into another block and mess up
        // the animation.
        entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
    }

    @Override
    public void endCraft(ItemEntity entity, ArrayList<BlockPos> cores, Recipe<?> recipe) {
        //Stop the timers so the crafting animation ends.
        cores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof DepositionCoreEntity depositionCore){
                depositionCore.setMaxCraftingTimer(0);
                depositionCore.setCraftingTimer(0);
                depositionCore.setCraftingEntity(null);
            }
        });

        if (recipe.getType() != TideRecipes.DEPOSITION.get()){
            return;
        }
        if (entity.isRemoved()){
            return;
        }

        ItemStack mainIngredient = entity.getItem();
        ItemStack[] depositionCatalysts = cores.stream()
                .map(pos -> ((DepositionCoreEntity) level.getBlockEntity(pos)).getItemStack())
                .filter(itemStack -> itemStack != null && !itemStack.isEmpty())
                .toArray(ItemStack[]::new);

        IItemHandlerModifiable inputs = new ItemStackHandler(depositionCatalysts.length + 1);
        inputs.setStackInSlot(0, mainIngredient);
        for (int i=0;i<depositionCatalysts.length;i++){
            inputs.setStackInSlot(i+1, depositionCatalysts[i]);
        }
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        //Check if we still have the ingredients to craft the thing.
        if (!((DepositionRecipe) recipe).matches(inputWrapper, level)){
            return;
        }

        //Craft the thing.
        ItemStack output = ((DepositionRecipe) recipe).assemble(inputWrapper, level.registryAccess());

        mainIngredient.split(1);
        for (var itemStack : depositionCatalysts){
            itemStack.split(1);
        }
        Vec3 pos = entity.position();
        ItemEntity outputEntity = new ItemEntity(level, pos.x,
                this.getBlockPos().getY() + 1, pos.z, output);
        outputEntity.setDeltaMovement(0, 0, 0);
        outputEntity.setNoGravity(true);
        outputEntity.setPickUpDelay(20);
        level.addFreshEntity(outputEntity);

        ((ServerLevel) level).sendParticles(TideParticles.SPLASH.get(),
                pos.x,
                pos.y + 1,
                pos.z,
                10,
                0,
                0,
                0,
                0.1);
    }
    /* ===end Crafting=== */
}
