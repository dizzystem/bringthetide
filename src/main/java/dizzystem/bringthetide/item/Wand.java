package dizzystem.bringthetide.item;

import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.MultiblockChecker;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Wand extends Item {
    public Wand(){
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (MultiblockChecker.assembleMultiblock(level, pos)){
            return InteractionResult.SUCCESS;
        }

        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack) &&
                    PoolHandler.wandUse(player, itemstack, level, blockpos1)) {
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public abstract int getWandPower();

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(item, level, tooltip, flag);
        tooltip.add(Component.translatable("Wand Power: ")
                .append(((Integer) getWandPower()).toString()).withStyle(ChatFormatting.AQUA));
    }
}
