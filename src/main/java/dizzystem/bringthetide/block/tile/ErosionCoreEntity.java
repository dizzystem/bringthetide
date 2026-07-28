package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.client.particle.DropletParticleType;
import dizzystem.bringthetide.entity.RitualTnt;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.registration.TideRecipes;
import dizzystem.bringthetide.util.OngoingCraft;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class ErosionCoreEntity extends FluidCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(-1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(-1, 0, -1), Blocks.CUT_SANDSTONE)
    );

    public ErosionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.EROSION_CORE_ENTITY.get(), blockPos, blockState);
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
        if (!this.isPoolActive()){
            return;
        }
        if (this.craftingEntity != null && !this.craftingEntity.isRemoved()){
            return;
        }

        PoolHandler.attemptCraft(itemEntity, level, getBlockPos(), TideRecipes.EROSION.get());
    }

    /**
     * Checks if the all the required fluids have been provided.
     * @param required A list of required FluidStacks.
     * @param provided A list of provided FluidStacks.
     */
    private boolean matchFluids(ArrayList<FluidStack> required, ArrayList<FluidStack> provided){
        Map<Fluid, Integer> left = required.stream().collect(Collectors.toMap(FluidStack::getFluid, FluidStack::getAmount));

        for (FluidStack fluidStack : provided){
            Fluid fluid = fluidStack.getFluid();
            if (left.containsKey(fluid)){
                int amt = left.get(fluid) - fluidStack.getAmount();
                if (amt > 0){
                    left.put(fluid, amt);
                } else {
                    left.remove(fluid);
                }
            }
        }

        return left.isEmpty();
    }

    /**
     * Deducts the required fluids from the fluids provided.
     * @param required A list of required FluidStacks.
     * @param provided A list of provided FluidStacks.
     */
    private void useFluids(ArrayList<FluidStack> required, ArrayList<FluidStack> provided){
        Map<Fluid, Integer> left = required.stream().collect(Collectors.toMap(FluidStack::getFluid, FluidStack::getAmount));

        for (FluidStack fluidStack : provided){
            Fluid fluid = fluidStack.getFluid();
            if (left.containsKey(fluid)){
                int amt = fluidStack.getAmount() - left.get(fluid);
                if (amt >= 0){
                    fluidStack.setAmount(amt);
                    left.remove(fluid);
                } else {
                    fluidStack.setAmount(0);
                    left.put(fluid, -amt);
                }
            }
        }
    }

    /* ===Crafting=== */
    @Override
    public void beginCraft(ItemEntity entity, ArrayList<BlockPos> cores) {
        //Check if we can craft anything with this entity and these cores.
        ItemStack itemStack = entity.getItem();
        IItemHandlerModifiable inputs = new ItemStackHandler(1);
        inputs.setStackInSlot(0, itemStack);
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        List<ErosionRecipe> possibleRecipes =
                this.level.getRecipeManager().getRecipesFor(
                        TideRecipes.EROSION.get(),
                        inputWrapper,
                        level);
        //We also do smelting recipes if supplied with lava.
        List<SmeltingRecipe> possibleSmeltingRecipes =
                this.level.getRecipeManager().getRecipesFor(
                        RecipeType.SMELTING,
                        inputWrapper,
                        level);
        possibleRecipes.addAll(possibleSmeltingRecipes.stream()
                .map(ErosionRecipe::new)
                .collect(Collectors.toCollection(ArrayList::new))) ;

        if (possibleRecipes.isEmpty()) {
            return;
        }

        ArrayList<BlockPos> involvedCores = cores.stream()
                .filter(pos -> (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore) &&
                        erosionCore.getFluid() != null)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<FluidStack> involvedFluids = involvedCores.stream()
                .map(pos -> (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore)
                        ? erosionCore.getFluid() : null)
                .collect(Collectors.toCollection(ArrayList::new));

        ErosionRecipe recipe = null;
        for (ErosionRecipe possibleRecipe : possibleRecipes){
            ArrayList<FluidStack> fluids = possibleRecipe.getFluidIngredients();
            if (matchFluids(fluids, involvedFluids)){
                recipe = possibleRecipe;
                break;
            }
        }
        if (recipe == null){
            return;
        }

        //If so, add a timer to each of the cores involved in the recipe to start the crafting animation.
        int timer = (int) (recipe.getCraftingTime() / getSpeed());
        involvedCores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore){
                erosionCore.setMaxCraftingTimer(timer);
                erosionCore.setCraftingTimer(timer);
                erosionCore.setCraftingEntity(entity);
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
        entity.setDeltaMovement(0, Math.min(0, entity.getDeltaMovement().y()), 0);
    }

    @Override
    public void endCraft(ItemEntity entity, ArrayList<BlockPos> cores, Recipe<?> unspecifiedRecipe) {
        //Stop the timers so the crafting animation ends.
        cores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore){
                erosionCore.setMaxCraftingTimer(0);
                erosionCore.setCraftingTimer(0);
                erosionCore.setCraftingEntity(null);
                BlockState blockState = level.getBlockState(pos);
                level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            }
        });

        if (unspecifiedRecipe.getType() != TideRecipes.EROSION.get()){
            return;
        }
        if (entity.isRemoved()){
            return;
        }
        ErosionRecipe recipe = (ErosionRecipe) unspecifiedRecipe;

        ItemStack itemStack = entity.getItem();
        IItemHandlerModifiable inputs = new ItemStackHandler(1);
        inputs.setStackInSlot(0, itemStack);
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        //Check if we still have the ingredients to craft the thing.
        if (!recipe.matches(inputWrapper, level)){
            return;
        }
        ArrayList<FluidStack> involvedFluids = cores.stream()
                .map(pos -> (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore)
                        ? erosionCore.getFluid() : null)
                .collect(Collectors.toCollection(ArrayList::new));
        if (!matchFluids(recipe.getFluidIngredients(), involvedFluids)){
            return;
        }

        //Craft the thing.
        ItemStack output = ((ErosionRecipe) recipe).assemble(inputWrapper, level.registryAccess());

        itemStack.split(1);
        useFluids(recipe.getFluidIngredients(), involvedFluids);
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
                0.25);
    }
    /* ===end Crafting=== */

    public void tickClient() {
        super.tickClient();

        if (!this.isPoolActive()) {
            return;
        }

        Level level = getLevel();
        RandomSource random = level.getRandom();
        if (this.craftingEntity != null && !this.getFluid().isEmpty() &&
                random.nextInt(4) == 0){
            FluidStack fluid = this.getFluid();
            Vec3 from = this.craftingEntity.position().add(0, -0.25, 0).add(
                    0.25 - 0.5 * random.nextFloat(),
                    0,
                    0.25 - 0.5 * random.nextFloat()
            );
            Vec3 to = this.craftingEntity.position().add(0, 0.5, 0);

            Vec3 towards = to.subtract(from).scale(0.25);
            level.addParticle(new DropletParticleType(fluid, false),
                    from.x,
                    from.y,
                    from.z,
                    towards.x,
                    towards.y,
                    towards.z);
        }
    }
}
