package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.BuddingPrismarine;
import dizzystem.bringthetide.block.DepositionCore;
import dizzystem.bringthetide.block.ErosionCore;
import dizzystem.bringthetide.block.PrismarineClusterBlock;
import dizzystem.bringthetide.fluid.BlockImbuedSeawater;
import dizzystem.bringthetide.tile.DepositionCoreEntity;
import dizzystem.bringthetide.tile.ErosionCoreEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TideBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            BringTheTide.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, BringTheTide.MODID);

    public static final RegistryObject<Block> BUDDING_PRISMARINE = BLOCKS.register("budding_prismarine",
            BuddingPrismarine::new);
    public static final BlockBehaviour.Properties clusterBehaviour = BlockBehaviour.Properties.of().mapColor(
                    MapColor.COLOR_BLUE).forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER)
            .strength(1.5F).lightLevel((state) -> {
                return 5;
            });
    public static final RegistryObject<Block> SMALL_PRISMARINE_BUD = BLOCKS.register("small_prismarine_bud",
            () -> new PrismarineClusterBlock(3, 4, clusterBehaviour.sound(SoundType.SMALL_AMETHYST_BUD)
                    .forceSolidOn().lightLevel((state) -> {
                        return 1;
                    })));
    public static final RegistryObject<Block> MEDIUM_PRISMARINE_BUD = BLOCKS.register("medium_prismarine_bud",
            () -> new PrismarineClusterBlock(4, 3, clusterBehaviour.sound(SoundType.LARGE_AMETHYST_BUD)
                    .forceSolidOn().lightLevel((state) -> {
                        return 2;
                    })));
    public static final RegistryObject<Block> LARGE_PRISMARINE_BUD = BLOCKS.register("large_prismarine_bud",
            () -> new PrismarineClusterBlock(5, 3, clusterBehaviour.sound(SoundType.MEDIUM_AMETHYST_BUD)
                    .forceSolidOn().lightLevel((state) -> {
                        return 4;
                    })));
    public static final RegistryObject<Block> PRISMARINE_CLUSTER = BLOCKS.register("prismarine_cluster",
            () -> new PrismarineClusterBlock(7, 3, clusterBehaviour));
    public static final RegistryObject<RotatedPillarBlock> DRIFTWOOD_LOG = BLOCKS.register("driftwood_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2).sound(SoundType.WOOD)
                    .instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistryObject<Block> EROSION_CORE = BLOCKS.register("erosion_core",
            ErosionCore::new);
    public static final RegistryObject<Block> DEPOSITION_CORE = BLOCKS.register("deposition_core",
            DepositionCore::new);
    public static final RegistryObject<LiquidBlock> BLOCK_IMBUED_SEAWATER = BLOCKS.register(
            "block_imbued_seawater", BlockImbuedSeawater::new);

    public static final RegistryObject<BlockEntityType<ErosionCoreEntity>> EROSION_CORE_ENTITY =
            BLOCK_ENTITIES.register("erosion_core_entity",
                    () -> BlockEntityType.Builder.of(ErosionCoreEntity::new, EROSION_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<DepositionCoreEntity>> DEPOSITION_CORE_ENTITY =
            BLOCK_ENTITIES.register("deposition_core_entity",
                    () -> BlockEntityType.Builder.of(DepositionCoreEntity::new, DEPOSITION_CORE.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
