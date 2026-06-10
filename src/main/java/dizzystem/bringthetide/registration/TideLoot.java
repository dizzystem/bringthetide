package dizzystem.bringthetide.registration;

import com.mojang.serialization.Codec;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.TideLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TideLoot {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BringTheTide.MODID);

    public static final Supplier<Codec<TideLootModifier>> TIDE_LOOT_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("tide_loot_modifier", () -> TideLootModifier.CODEC);

    public static void init(IEventBus modEventBus) {
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
    }
}
