package dizzystem.bringthetide.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

public class VortexCoreEntity extends CoreEntity {
    public int ATTACK_COOLDOWN = 80; //4 seconds
    public static final String PLACEDBY_TAG = "Placed By";
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(2, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -2), Blocks.SANDSTONE),
            entry(new Vec3i(3, 0, -3), Blocks.SANDSTONE)
    );
    UUID placedBy;
    private final Map<Entity, Integer> entityCooldowns = new HashMap<>();

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

        int ticks = this.ticks;
        Integer cooldown = this.entityCooldowns.get(livingEntity);
        if (cooldown != null && cooldown > ticks){
            return;
        }
        this.entityCooldowns.put(livingEntity, ticks + ATTACK_COOLDOWN);

        LogUtils.getLogger().info("simulating attack from {}", placedBy);
        FakePlayer fp;
        if (this.placedBy != null){
            fp = FakePlayerFactory.get((ServerLevel) level, new GameProfile(this.placedBy, null));
        } else {
            fp = FakePlayerFactory.get((ServerLevel) level, new GameProfile(null, "TideFakePlayer"));
        }

        //knock them towards the centre, to avoid the drops flying out of the pool
        Vec3 centre = getPoolCentre().getCenter();
        Vec3 entityPos = livingEntity.position();
        Vec3 attackPos = entityPos.add(entityPos.subtract(centre).normalize());
        fp.setPos(attackPos);
        livingEntity.hurt(livingEntity.damageSources().playerAttack(fp), 6);
        livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().scale(0.5));

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
