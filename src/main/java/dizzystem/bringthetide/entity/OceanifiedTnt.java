package dizzystem.bringthetide.entity;

import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public class OceanifiedTnt extends PrimedTnt {
    BlockPos poolBlock;

    public OceanifiedTnt(EntityType<? extends OceanifiedTnt> entityType, Level level, BlockPos poolBlock) {
        super(entityType, level);

        this.poolBlock = poolBlock;
    }

    public OceanifiedTnt(EntityType<? extends OceanifiedTnt> entityType, Level level) {
        this(entityType, level, null);
    }

    public BlockPos getPoolBlock(){
        return poolBlock;
    }

    protected void explode() {
        Level level = level();
        PoolHandler.aoeApplyRitualEffect(level, this, 4.0f);
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
