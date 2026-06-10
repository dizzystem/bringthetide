package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.fluid.FluidImbuedSeawater;
import dizzystem.bringthetide.fluid.TypeImbuedSeawater;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TideFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS,
            BringTheTide.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
            ForgeRegistries.Keys.FLUID_TYPES, BringTheTide.MODID);


    public static final RegistryObject<ForgeFlowingFluid> IMBUED_SEAWATER = FLUIDS.register(
            "imbued_seawater", FluidImbuedSeawater.Source::new);
    public static final RegistryObject<ForgeFlowingFluid> FLOWING_IMBUED_SEAWATER = FLUIDS.register(
            "flowing_imbued_seawater", FluidImbuedSeawater.Flowing::new);
    public static final RegistryObject<FluidType> TYPE_IMBUED_SEAWATER = FLUID_TYPES.register(
            "type_imbued_seawater", TypeImbuedSeawater::new);

    public static void init(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
    }
}
