package dizzystem.bringthetide.tile;

import com.mojang.datafixers.util.Pair;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class BasinCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, -2), Blocks.PRISMARINE),
            entry(new Vec3i(-2, 0, -2), Blocks.PRISMARINE),
            entry(new Vec3i(-2, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(-2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(2, 0, -2), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, -2), Blocks.PRISMARINE)
    );

    public BasinCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.BASIN_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    /**
     * Tries to brew the given starting potion with the given ingredients until either all the ingredients
     *  are used up or can't be used.
     * @return a pair containing the result potion and the ingredients used to make it
     */
    public static @NotNull Pair<ItemStack, ArrayList<ItemStack>> tryBrewPotion(ItemStack startPotion,
                                                                               ArrayList<ItemStack> ingredients) {
        NonNullList<ItemStack> potions = NonNullList.create();
        potions.add(startPotion);

        int ingredientIndex = 0;
        boolean changed = false;
        ArrayList<ItemStack> used = new ArrayList<>();

        //just loop through and try each ingredient until something sticks
        for (int i=0;i<100;i++){
            ItemStack ingredient = ingredients.get(ingredientIndex);

            if (BrewingRecipeRegistry.canBrew(potions, ingredient, new int[]{0})){
                changed = true;
                BrewingRecipeRegistry.brewPotions(potions, ingredient, new int[]{0});
                used.add(ingredient);
                ingredients.remove(ingredientIndex);
                ingredientIndex = 0;
                if (ingredients.size() <= 0){
                    //used everything, we're done :)
                    break;
                }
                continue;
            }

            ingredientIndex ++;
            if (ingredientIndex == ingredients.size()){
                if (!changed){
                    //did a loop without any change, we can't brew any further
                    break;
                }
                ingredientIndex = 0;
                changed = false;
            }
        }

        ItemStack endPotion = potions.get(0);
        return new Pair<>(endPotion, used);
    }

    /**
     * Checks if the entity needs a new dose of effect administered.
     */
    public boolean shouldApplyEffect(LivingEntity livingEntity, MobEffectInstance effect) {
        MobEffect effectType = effect.getEffect();

        if (effectType.isInstantenous()) {
            return true;
        } else {
            MobEffectInstance existingEffect = livingEntity.getEffect(effectType);
            //replace effect a little early to avoid running out
            //night vision starts pulsing at 10s = 200t, so replace that even earlier
            if (existingEffect == null ||
                    existingEffect.endsWithin(100) ||
                    effectType == MobEffects.NIGHT_VISION && existingEffect.endsWithin(400)){
                return true;
            }
        }

        return false;
    }

    /**
     * Applies the given effect to the given living entity.
     */
    public void applyEffect(LivingEntity livingEntity, MobEffectInstance effect) {
        MobEffect effectType = effect.getEffect();

        if (effectType.isInstantenous()) {
            effectType.applyInstantenousEffect(null, null, livingEntity, effect.getAmplifier(), 1.0D);
        } else {
            livingEntity.addEffect(effect);
        }
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos){
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        //simulate brewing a potion
        ArrayList<ItemStack> ingredients = this.getPoolCores().stream().map(
                corePos -> (level.getBlockEntity(corePos) instanceof BasinCoreEntity turbulenceCoreEntity)
                        ? turbulenceCoreEntity.getItemStack()
                        : ItemStack.EMPTY
        ).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        if (ingredients.size() <= 0){
            return;
        }
        Collections.shuffle(ingredients);

        ItemStack startPotion = new ItemStack(Items.POTION, 1);
        PotionUtils.setPotion(startPotion, Potions.AWKWARD);

        Pair<ItemStack, ArrayList<ItemStack>> brewResult = tryBrewPotion(startPotion, ingredients);
        ItemStack endPotion = brewResult.getFirst();
        ArrayList<ItemStack> used = brewResult.getSecond();

        //try to apply the potion
        boolean applied = false;
        RandomSource random = level.getRandom();

        if (endPotion.equals(startPotion)){
            //couldn't brew anything, see if it has a food eat effect instead
            used.clear();
            for (ItemStack ingredient : ingredients){
                FoodProperties props = ingredient.getFoodProperties(livingEntity);
                if (props == null){
                    continue;
                }
                boolean ingredientApplied = false;
                for (Pair<MobEffectInstance, Float> effectChancePair : props.getEffects()){
                    MobEffectInstance effect = effectChancePair.getFirst();
                    float chance = effectChancePair.getSecond();

                    if (this.shouldApplyEffect(livingEntity, effect)){
                        if (random.nextFloat() < chance){
                            this.applyEffect(livingEntity, effect);
                        }
                        ingredientApplied = true;
                    }
                }
                if (ingredientApplied){
                    used.add(ingredient); //use up the item even if we fail the chance
                    applied = true;
                }
            }
        } else {
            for (MobEffectInstance effect : PotionUtils.getMobEffects(endPotion)) {
                //double the duration, to compensate for using 3 potions' worth of ingredients
                MobEffectInstance doubledEffect = new MobEffectInstance(
                        effect.getEffect(),
                        effect.getDuration() * 2,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                );
                doubledEffect.setCurativeItems(effect.getCurativeItems());

                if (this.shouldApplyEffect(livingEntity, doubledEffect)){
                    this.applyEffect(livingEntity, doubledEffect);
                    applied = true;
                }
            }
        }

        if (applied){
            for (var item : used){
                item.shrink(1);
            }

            BlockPos corePos = getBlockPos();
            BlockState blockState = level.getBlockState(corePos);
            level.sendBlockUpdated(pos, blockState, blockState, Block.UPDATE_CLIENTS);
            ((ServerLevel) level).sendParticles(TideParticles.BUBBLE.get(),
                    pos.getX() + 0.5,
                    getBlockPos().getY() + 1,
                    pos.getZ() + 0.5,
                    4,
                    0,
                    0,
                    0,
                    0.4);
        }
    }
}
