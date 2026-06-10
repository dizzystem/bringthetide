package dizzystem.bringthetide.item;

import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.util.MultiblockChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class Wand extends Item {
    public Wand (){
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context){
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide()){
            ((ServerLevel) level).sendParticles(
                    TideParticles.SPLASH.get(),
                    pos.getX() + .5,
                    pos.getY() + .5,
                    pos.getZ() + .5,
                    10,
                    0,
                    0,
                    0,
                    0.15);
        }

        if (MultiblockChecker.assembleMultiblock(level, pos)){
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
