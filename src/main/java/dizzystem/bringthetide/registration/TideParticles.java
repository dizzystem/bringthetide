package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.client.particle.DropletParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TideParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(
            ForgeRegistries.PARTICLE_TYPES, BringTheTide.MODID);

    public static final Supplier<SimpleParticleType> WHIRLPOOL = PARTICLE_TYPES.register("whirlpool",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SPLASH = PARTICLE_TYPES.register("splash",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BUBBLE = PARTICLE_TYPES.register("bubble",
            () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SPARKLE = PARTICLE_TYPES.register("sparkle",
            () -> new SimpleParticleType(false));
    public static final Supplier<DropletParticleType> DROPLET = PARTICLE_TYPES.register("droplet",
            () -> new DropletParticleType(false));

    public static void init(IEventBus modEventBus) {
        PARTICLE_TYPES.register(modEventBus);
    }
}
