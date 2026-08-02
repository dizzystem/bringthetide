package dizzystem.bringthetide.entity;

import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

public class RitualTnt extends PrimedTnt {
    BlockPos poolBlock;

    public RitualTnt(EntityType<? extends RitualTnt> entityType, Level level, BlockPos poolBlock) {
        super(entityType, level);

        this.poolBlock = poolBlock;
    }

    public RitualTnt(EntityType<? extends RitualTnt> entityType, Level level) {
        this(entityType, level, null);
    }

    public BlockPos getPoolBlock(){
        return this.poolBlock;
    }

    protected void explode() {
        ServerLevel level = (ServerLevel) level();
        PoolHandler.aoeApplyRitualEffect(level, this, 4.0f);
        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, getX(), getY(), getZ(), 1.0D, 0.0D, 0.0D);

        for (ServerPlayer serverplayer : level.players()) {
            if (serverplayer.distanceToSqr(getX(), getY(), getZ()) < 4096.0D) {
                serverplayer.connection.send(new ClientboundExplodePacket(getX(), getY(), getZ(), 4.0f, new ArrayList<>(), null));
            }
        }
    }

    @ParametersAreNonnullByDefault
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (this.poolBlock != null){
            tag.putLong("poolBlock", this.poolBlock.asLong());
        }
    }

    @ParametersAreNonnullByDefault
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        long poolBlockLong = tag.getLong("poolBlock");
        if (poolBlockLong != 0){
            this.poolBlock = BlockPos.of(poolBlockLong);
        }
    }
}
