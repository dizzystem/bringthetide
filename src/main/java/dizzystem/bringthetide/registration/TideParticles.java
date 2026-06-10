package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
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

    public static void init(IEventBus modEventBus) {
        PARTICLE_TYPES.register(modEventBus);
    }
}
