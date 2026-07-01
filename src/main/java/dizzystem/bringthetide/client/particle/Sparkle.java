package dizzystem.bringthetide.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class Sparkle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final int MAX_LIFE = 40;

    public Sparkle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet){
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.lifetime = MAX_LIFE;
        this.setColor(1f, 1f, 1f);
        this.scale(0.25f);

        //initialize to prevent crash
        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        setSpriteFromAge(spriteSet);
        super.tick();
        this.move(0f,0.02f,0f);
        this.alpha = Math.min(1f, 2f * (MAX_LIFE - this.age) / MAX_LIFE);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
