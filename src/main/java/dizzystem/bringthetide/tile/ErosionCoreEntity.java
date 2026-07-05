package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.client.particle.DropletParticleType;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class ErosionCoreEntity extends FluidCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(-1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(-1, 0, -1), Blocks.SANDSTONE)
    );

    public ErosionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.EROSION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos){
        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }
        if (!this.isPoolActive()){
            return;
        }

        PoolHandler.attemptCraft(itemEntity, level, getBlockPos(), TideRecipes.EROSION.get());
    }

    /* ===Crafting=== */
    @Override
    public void beginCraft(ItemEntity entity, ArrayList<BlockPos> cores) {
        //Check if we can craft anything with this entity and these cores.
        ItemStack itemStack = entity.getItem();
        IItemHandlerModifiable inputs = new ItemStackHandler(1);
        inputs.setStackInSlot(0, itemStack);
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        Optional<ErosionRecipe> maybeRecipe =
                this.level.getRecipeManager().getRecipeFor(
                        TideRecipes.EROSION.get(),
                        inputWrapper,
                        level);
        if (!maybeRecipe.isPresent()) {
            return;
        }

        //If so, add a timer to each of the cores involved in the recipe to start the crafting animation.
        ArrayList<BlockPos> involvedCores = cores.stream()
                .filter(pos -> (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore) &&
                        erosionCore.getFluid() != null)
                .collect(Collectors.toCollection(ArrayList::new));

        ErosionRecipe recipe = maybeRecipe.get();
        involvedCores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore){
                erosionCore.setMaxCraftingTimer(recipe.getCraftingTime() / getSpeed());
                erosionCore.setCraftingTimer(recipe.getCraftingTime() / getSpeed());
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
        entity.setDeltaMovement(0, entity.getDeltaMovement().y(), 0);
    }

    @Override
    public void endCraft(ItemEntity entity, ArrayList<BlockPos> cores, Recipe<?> recipe) {
        //Stop the timers so the crafting animation ends.
        cores.forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof ErosionCoreEntity erosionCore){
                erosionCore.setMaxCraftingTimer(0);
                erosionCore.setCraftingTimer(0);
                erosionCore.setCraftingEntity(null);
            }
        });

        if (recipe.getType() != TideRecipes.EROSION.get()){
            return;
        }
        if (entity.isRemoved()){
            return;
        }

        ItemStack itemStack = entity.getItem();
        IItemHandlerModifiable inputs = new ItemStackHandler(1);
        inputs.setStackInSlot(0, itemStack);
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        //Check if we still have the ingredients to craft the thing.
        if (!((ErosionRecipe) recipe).matches(inputWrapper, level)){
            return;
        }

        //Craft the thing.
        ItemStack output = ((ErosionRecipe) recipe).assemble(inputWrapper, level.registryAccess());

        itemStack.split(1);
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

    public void tickClient() {
        super.tickClient();

        if (!this.isPoolActive()) {
            return;
        }

        Level level = getLevel();
        RandomSource random = level.getRandom();
        if (this.craftingEntity != null && !this.getFluid().isEmpty() && random.nextInt(5) == 0){
            Vec3 bubblePos = getBlockPos().getCenter().add(0, 0.8, 0).add(
                    0.25 - 0.5 * random.nextFloat(),
                    0.25 - 0.5 * random.nextFloat(),
                    0.25 - 0.5 * random.nextFloat()
            );
            FluidStack fluid = this.getFluid();
            Vec3 centre = this.craftingEntity.position().add(0, 0.5, 0).add(
                    0.25 - 0.5 * random.nextFloat(),
                    0.25 - 0.5 * random.nextFloat(),
                    0.25 - 0.5 * random.nextFloat()
            );

            Vec3 towardsCentre = centre.subtract(bubblePos).scale(0.1);
            level.addParticle(TideParticles.DROPLET.get(),
                    bubblePos.x,
                    bubblePos.y,
                    bubblePos.z,
                    towardsCentre.x,
                    towardsCentre.y,
                    towardsCentre.z);
        }
    }
}
