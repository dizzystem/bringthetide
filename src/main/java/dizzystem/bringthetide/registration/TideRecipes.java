package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.recipe.DepositionRecipe;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TideRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, BringTheTide.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BringTheTide.MODID);

    public static final Supplier<RecipeType<ErosionRecipe>> EROSION = RECIPE_TYPES.register(
            "erosion",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion"))
    );
    public static final Supplier<RecipeSerializer<ErosionRecipe>> EROSION_SERIALIZER =
            RECIPE_SERIALIZERS.register("erosion", ErosionRecipe.Serializer::new);

    public static final Supplier<RecipeType<DepositionRecipe>> DEPOSITION = RECIPE_TYPES.register(
            "deposition",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition"))
    );
    public static final Supplier<RecipeSerializer<DepositionRecipe>> DEPOSITION_SERIALIZER =
            RECIPE_SERIALIZERS.register("deposition", DepositionRecipe.Serializer::new);

    public static void init(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
