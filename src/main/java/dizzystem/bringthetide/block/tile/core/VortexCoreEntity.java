package dizzystem.bringthetide.block.tile.core;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.entity.RitualTnt;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.FakePlayerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

public class VortexCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(2, 0, -2), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(3, 0, -2), Blocks.CUT_SANDSTONE),
            entry(new Vec3i(3, 0, -3), Blocks.CUT_SANDSTONE)
    );
    private final ItemStack weapon = new ItemStack(Items.IRON_SWORD);

    public VortexCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.VORTEX_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    @Override
    public void entityInPool(Entity entity, Level level, BlockPos pos, RitualTnt tnt) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        UUID placedBy = this.getPlacedBy();
        LogUtils.getLogger().info("simulating attack from {} with looting {}", placedBy, this.getLuck());
        FakePlayer fp = FakePlayerHandler.getFakePlayer((ServerLevel) level, placedBy);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.MOB_LOOTING, (int) this.getLuck()), weapon);
        fp.setItemInHand(InteractionHand.MAIN_HAND, weapon);

        //knock them towards the centre, to avoid the drops flying out of the pool
        Vec3 centre = getPoolCentre().getCenter();
        Vec3 entityPos = livingEntity.position();
        Vec3 attackPos = entityPos.add(entityPos.subtract(centre).normalize());
        fp.setPos(attackPos);
        livingEntity.hurt(livingEntity.damageSources().playerAttack(fp), 6);
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().scale(0.5));
        fp.setPos(0, 0, 0);

        ((ServerLevel) level).sendParticles(TideParticles.BUBBLE.get(),
                attackPos.x,
                getBlockPos().getY() + 1,
                attackPos.z,
                4,
                0,
                0,
                0,
                0.4);
    }
}
