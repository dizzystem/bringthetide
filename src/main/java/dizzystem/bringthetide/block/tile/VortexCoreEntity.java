package dizzystem.bringthetide.block.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.FakePlayerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

public class VortexCoreEntity extends CoreEntity {
    public static final String PLACEDBY_TAG = "Placed By";
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -3), Blocks.SANDSTONE)
    );
    private final ItemStack weapon = new ItemStack(Items.IRON_SWORD);
    UUID placedBy;

    public VortexCoreEntity(BlockPos blockPos, BlockState blockState, UUID placedBy){
        super(TideBlocks.VORTEX_CORE_ENTITY.get(), blockPos, blockState);
        this.placedBy = placedBy;
    }

    public VortexCoreEntity(BlockPos blockPos, BlockState blockState){
        this(blockPos, blockState, null);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        if (!this.isPoolActive()) {
            return;
        }

        LogUtils.getLogger().info("simulating attack from {}", placedBy);
        FakePlayer fp = FakePlayerHandler.getFakePlayer((ServerLevel) level, this.placedBy);
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

    @Override
    protected void saveClientData(CompoundTag tag) {
        super.saveClientData(tag);
        //save the player that placed us
        if (this.placedBy != null){
            tag.putString(PLACEDBY_TAG, this.placedBy.toString());
        }
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        super.loadClientData(tag);
        if (tag.contains(PLACEDBY_TAG)) {
            //load the player that placed us
            this.placedBy = UUID.fromString(tag.getString(PLACEDBY_TAG));
        }
    }
}
