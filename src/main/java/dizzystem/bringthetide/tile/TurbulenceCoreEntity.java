package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class TurbulenceCoreEntity extends ItemCoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE)
    );

    public TurbulenceCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.TURBULENCE_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
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
                corePos -> (level.getBlockEntity(corePos) instanceof TurbulenceCoreEntity turbulenceCoreEntity)
                        ? turbulenceCoreEntity.getItemStack()
                        : ItemStack.EMPTY
        ).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        if (ingredients.size() <= 0){
            return;
        }

        ItemStack startPotion = new ItemStack(Items.POTION, 1);
        PotionUtils.setPotion(startPotion, Potions.AWKWARD);
        NonNullList<ItemStack> potions = NonNullList.create();
        potions.add(startPotion);

        //just loop through and try each ingredient until something sticks
        int ingredientIndex = 0;
        boolean changed = false;
        ArrayList<ItemStack> used = new ArrayList<>();
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

        //try to apply the potion
        boolean applied = false;
        for (MobEffectInstance effect : PotionUtils.getMobEffects(endPotion)) {
            MobEffect effectType = effect.getEffect();
            if (effectType.isInstantenous()) {
                effectType.applyInstantenousEffect(null, null, livingEntity, effect.getAmplifier(), 1.0D);
                applied = true;
            } else {
                MobEffectInstance existingEffect = livingEntity.getEffect(effectType);
                //replace effect a little early to avoid running out
                //night vision starts pulsing at 10s = 200t, so replace that even earlier
                if (existingEffect == null ||
                        existingEffect.endsWithin(100) ||
                        effectType == MobEffects.NIGHT_VISION && existingEffect.endsWithin(400)){
                    MobEffectInstance newEffect = new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration() * 2,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    );
                    newEffect.setCurativeItems(effect.getCurativeItems());
                    livingEntity.addEffect(newEffect);
                    applied = true;
                }
            }
        }

        if (applied){
            for (var item : used){
                item.shrink(1);
            }

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
