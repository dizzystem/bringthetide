package dizzystem.bringthetide.client.particle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class Whirlpool extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float MAX_LIFE = 10f;

    public Whirlpool(ClientLevel level, double x, double y, double z, SpriteSet spriteSet){
        super(level, x, y, z);

        this.spriteSet = spriteSet;
        this.lifetime = (int)(MAX_LIFE / (this.random.nextFloat() * 0.9f + 0.1f));
        this.gravity = (float) 0.04f;

        //initialize to prevent crash
        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick(){
        float DIVISOR_CONST = (float) (MAX_LIFE / Math.PI);

        setSpriteFromAge(spriteSet);
        super.tick();

        float time = (MAX_LIFE + this.age - this.lifetime) / DIVISOR_CONST;
        this.move(
                (Math.cos(time) - time * Math.sin(time)) / 8 / DIVISOR_CONST,
                0.04f,
                (Math.sin(time) + time * Math.cos(time)) / 8 / DIVISOR_CONST);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
