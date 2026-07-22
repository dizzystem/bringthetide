package dizzystem.bringthetide.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

//We need this to have particles that use a dynamic texture.
public class DropletParticleType extends ParticleType<DropletParticleType> implements ParticleOptions {
    public static final ParticleOptions.Deserializer<DropletParticleType> DESERIALIZER =
            new ParticleOptions.Deserializer<>(){
                public DropletParticleType fromCommand(ParticleType<DropletParticleType> type, StringReader reader)
                        throws CommandSyntaxException {
                    return (DropletParticleType) type;
                }

                public DropletParticleType fromNetwork(ParticleType<DropletParticleType> type, FriendlyByteBuf buf){
                    ((DropletParticleType) type).fluid = FluidStack.readFromPacket(buf);
                    return (DropletParticleType) type;
                }
            };
    public FluidStack fluid;

    public DropletParticleType(FluidStack fluid, boolean overrideLimiter) {
        super(overrideLimiter, DESERIALIZER);
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public final Codec<DropletParticleType> codec = Codec.unit(this::getType);

    @Override
    public Codec<DropletParticleType> codec() {
        return codec;
    }

    @Override
    public @NotNull DropletParticleType getType(){
        return TideParticles.DROPLET.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf){
        this.fluid.writeToPacket(buf);
    }

    @Override
    public String writeToString(){
        return this.fluid.toString();
    }
}