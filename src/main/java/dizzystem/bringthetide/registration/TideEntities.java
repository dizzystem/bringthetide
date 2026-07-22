package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.entity.OceanifiedTnt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TideEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            BringTheTide.MODID);

    public static final RegistryObject<EntityType<OceanifiedTnt>> OCEANIFIED_TNT = ENTITY_TYPES.register("oceanified_tnt",
            () -> EntityType.Builder.<OceanifiedTnt>of(OceanifiedTnt::new, MobCategory.MISC)
                    .build(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "oceanified_tnt").toString()));

    public static void init(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
