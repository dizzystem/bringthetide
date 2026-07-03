package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.*;
import dizzystem.bringthetide.fluid.BlockImbuedSeawater;
import dizzystem.bringthetide.tile.*;
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
    public static final RegistryObject<Block> CURRENT_CORE = BLOCKS.register("current_core",
            CurrentCore::new);
    public static final RegistryObject<Block> DEPOSITION_CORE = BLOCKS.register("deposition_core",
            DepositionCore::new);
    public static final RegistryObject<Block> EROSION_CORE = BLOCKS.register("erosion_core",
            ErosionCore::new);
    public static final RegistryObject<Block> SINKHOLE_CORE = BLOCKS.register("sinkhole_core",
            SinkholeCore::new);
    public static final RegistryObject<Block> TRAWL_CORE = BLOCKS.register("trawl_core",
            TrawlCore::new);
    public static final RegistryObject<Block> TURBULENCE_CORE = BLOCKS.register("turbulence_core",
            TurbulenceCore::new);
    public static final RegistryObject<Block> VORTEX_CORE = BLOCKS.register("vortex_core",
            VortexCore::new);
    public static final RegistryObject<LiquidBlock> BLOCK_IMBUED_SEAWATER = BLOCKS.register(
            "block_imbued_seawater", BlockImbuedSeawater::new);

    public static final RegistryObject<BlockEntityType<CurrentCoreEntity>> CURRENT_CORE_ENTITY =
            BLOCK_ENTITIES.register("current_core_entity",
                    () -> BlockEntityType.Builder.of(CurrentCoreEntity::new, CURRENT_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<DepositionCoreEntity>> DEPOSITION_CORE_ENTITY =
            BLOCK_ENTITIES.register("deposition_core_entity",
                    () -> BlockEntityType.Builder.of(DepositionCoreEntity::new, DEPOSITION_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ErosionCoreEntity>> EROSION_CORE_ENTITY =
            BLOCK_ENTITIES.register("erosion_core_entity",
                    () -> BlockEntityType.Builder.of(ErosionCoreEntity::new, EROSION_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<SinkholeCoreEntity>> SINKHOLE_CORE_ENTITY =
            BLOCK_ENTITIES.register("sinkhole_core_entity",
                    () -> BlockEntityType.Builder.of(SinkholeCoreEntity::new, SINKHOLE_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TrawlCoreEntity>> TRAWL_CORE_ENTITY =
            BLOCK_ENTITIES.register("trawl_core_entity",
                    () -> BlockEntityType.Builder.of(TrawlCoreEntity::new, TRAWL_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TurbulenceCoreEntity>> TURBULENCE_CORE_ENTITY =
            BLOCK_ENTITIES.register("turbulence_core_entity",
                    () -> BlockEntityType.Builder.of(TurbulenceCoreEntity::new, TURBULENCE_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<VortexCoreEntity>> VORTEX_CORE_ENTITY =
            BLOCK_ENTITIES.register("vortex_core_entity",
                    () -> BlockEntityType.Builder.of(VortexCoreEntity::new, VORTEX_CORE.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
