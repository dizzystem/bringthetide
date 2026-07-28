package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.*;
import dizzystem.bringthetide.block.tile.*;
import dizzystem.bringthetide.fluid.BlockImbuedSeawater;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
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
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD)
                    .instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE).ignitedByLava()));

    public static final BlockSetType DRIFTWOOD_BLOCK_TYPE = new BlockSetType("driftwood");
    public static final WoodType DRIFTWOOD_WOOD_TYPE = new WoodType("driftwood", DRIFTWOOD_BLOCK_TYPE);
    public static final RegistryObject<Block> DRIFTWOOD_PLANKS = BLOCKS.register("driftwood_planks",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0f, 3.0f).sound(SoundType.WOOD)
                    .instrument(NoteBlockInstrument.BASS).mapColor(MapColor.TERRACOTTA_WHITE).ignitedByLava()));
    public static final RegistryObject<StairBlock> DRIFTWOOD_STAIRS = BLOCKS.register("driftwood_stairs",
            () -> new StairBlock(DRIFTWOOD_PLANKS.get()::defaultBlockState, BlockBehaviour.Properties.copy(DRIFTWOOD_PLANKS.get())));
    public static final RegistryObject<SlabBlock> DRIFTWOOD_SLAB = BLOCKS.register("driftwood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(DRIFTWOOD_PLANKS.get())));
    public static final RegistryObject<FenceBlock> DRIFTWOOD_FENCE = BLOCKS.register("driftwood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(DRIFTWOOD_PLANKS.get())));
    public static final RegistryObject<FenceGateBlock> DRIFTWOOD_FENCE_GATE = BLOCKS.register("driftwood_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(DRIFTWOOD_PLANKS.get()), DRIFTWOOD_WOOD_TYPE));

//    public static final RegistryObject<StandingSignBlock> DRIFTWOOD_SIGN = BLOCKS.register("driftwood_sign",
//            () -> new StandingSignBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE)
//                    .instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().strength(1.0F).ignitedByLava(),
//                    DRIFTWOOD_WOOD_TYPE));
//    public static final RegistryObject<WallSignBlock> DRIFTWOOD_WALL_SIGN = BLOCKS.register("driftwood_wall_sign",
//            () -> new WallSignBlock(BlockBehaviour.Properties.copy(DRIFTWOOD_SIGN.get()), DRIFTWOOD_WOOD_TYPE));
//    public static final RegistryObject<CeilingHangingSignBlock> DRIFTWOOD_HANGING_SIGN = BLOCKS.register("driftwood_hanging_sign",
//            () -> new CeilingHangingSignBlock(BlockBehaviour.Properties.copy(DRIFTWOOD_SIGN.get()), DRIFTWOOD_WOOD_TYPE));

    public static final RegistryObject<PressurePlateBlock> DRIFTWOOD_PRESSURE_PLATE = BLOCKS.register("driftwood_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.of().forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission()
                            .strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY),
            DRIFTWOOD_BLOCK_TYPE));
    public static final RegistryObject<ButtonBlock> DRIFTWOOD_BUTTON = BLOCKS.register("driftwood_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F)
                    .pushReaction(PushReaction.DESTROY), DRIFTWOOD_BLOCK_TYPE, 30, true));


    public static final RegistryObject<Block> TANK = BLOCKS.register("fluid_tank",
            Tank::new);
    public static final RegistryObject<Block> RITUAL_TNT = BLOCKS.register("ritual_tnt",
            () -> new Block(BlockBehaviour.Properties.of()));

    private static Block.Properties coreProps = BlockBehaviour.Properties.of().strength(3.5F);
    public static final RegistryObject<Block> BASIN_CORE = BLOCKS.register("basin_core",
            () -> new Core(coreProps, BasinCoreEntity::new));
    public static final RegistryObject<Block> CURRENT_CORE = BLOCKS.register("current_core",
            () -> new Core(coreProps, CurrentCoreEntity::new));
    public static final RegistryObject<Block> DEPOSITION_CORE = BLOCKS.register("deposition_core",
            () -> new Core(coreProps, DepositionCoreEntity::new));
    public static final RegistryObject<Block> EROSION_CORE = BLOCKS.register("erosion_core",
            () -> new Core(coreProps, ErosionCoreEntity::new));
    public static final RegistryObject<Block> PELAGIC_CORE = BLOCKS.register("pelagic_core",
            () -> new Core(coreProps, PelagicCoreEntity::new));
    public static final RegistryObject<Block> REEF_CORE = BLOCKS.register("reef_core",
            () -> new Core(coreProps, ReefCoreEntity::new));
    public static final RegistryObject<Block> SEABED_CORE = BLOCKS.register("seabed_core",
            () -> new Core(coreProps, SeabedCoreEntity::new));
    public static final RegistryObject<Block> SINKHOLE_CORE = BLOCKS.register("sinkhole_core",
            () -> new Core(coreProps, SinkholeCoreEntity::new));
    public static final RegistryObject<Block> TRAWL_CORE = BLOCKS.register("trawl_core",
            () -> new Core(coreProps, TrawlCoreEntity::new));
    public static final RegistryObject<Block> VORTEX_CORE = BLOCKS.register("vortex_core",
            () -> new Core(coreProps, VortexCoreEntity::new));
    public static final RegistryObject<Block> WHIRLPOOL_CORE = BLOCKS.register("whirlpool_core",
            () -> new Core(coreProps, WhirlpoolCoreEntity::new));
    public static final RegistryObject<Block> FLOW_UPGRADE = BLOCKS.register("flow_upgrade",
            () -> new Core(coreProps, FlowUpgradeEntity::new));
    public static final RegistryObject<Block> BOUNTY_UPGRADE = BLOCKS.register("bounty_upgrade",
            () -> new Core(coreProps, BountyUpgradeEntity::new));
    public static final RegistryObject<Block> RANGE_UPGRADE = BLOCKS.register("range_upgrade",
            () -> new Core(coreProps, RangeUpgradeEntity::new));
    public static final RegistryObject<Block> ITEM_FILTER = BLOCKS.register("item_filter",
            () -> new Core(coreProps, ItemFilterEntity::new));
    public static final RegistryObject<Block> FLUID_FILTER = BLOCKS.register("fluid_filter",
            () -> new Core(coreProps, FluidFilterEntity::new));
    public static final RegistryObject<Block> ENTITY_FILTER = BLOCKS.register("entity_filter",
            () -> new Core(coreProps, EntityFilterEntity::new));
    public static final RegistryObject<LiquidBlock> BLOCK_IMBUED_SEAWATER = BLOCKS.register(
            "block_imbued_seawater", BlockImbuedSeawater::new);

    public static final RegistryObject<BlockEntityType<TankEntity>> TANK_ENTITY =
            BLOCK_ENTITIES.register("tank_entity",
                    () -> BlockEntityType.Builder.of(TankEntity::new, TANK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BasinCoreEntity>> BASIN_CORE_ENTITY =
            BLOCK_ENTITIES.register("basin_core_entity",
                    () -> BlockEntityType.Builder.of(BasinCoreEntity::new, BASIN_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CurrentCoreEntity>> CURRENT_CORE_ENTITY =
            BLOCK_ENTITIES.register("current_core_entity",
                    () -> BlockEntityType.Builder.of(CurrentCoreEntity::new, CURRENT_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<DepositionCoreEntity>> DEPOSITION_CORE_ENTITY =
            BLOCK_ENTITIES.register("deposition_core_entity",
                    () -> BlockEntityType.Builder.of(DepositionCoreEntity::new, DEPOSITION_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ErosionCoreEntity>> EROSION_CORE_ENTITY =
            BLOCK_ENTITIES.register("erosion_core_entity",
                    () -> BlockEntityType.Builder.of(ErosionCoreEntity::new, EROSION_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<PelagicCoreEntity>> PELAGIC_CORE_ENTITY =
            BLOCK_ENTITIES.register("pelagic_core_entity",
                    () -> BlockEntityType.Builder.of(PelagicCoreEntity::new, PELAGIC_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ReefCoreEntity>> REEF_CORE_ENTITY =
            BLOCK_ENTITIES.register("reef_core_entity",
                    () -> BlockEntityType.Builder.of(ReefCoreEntity::new, REEF_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<SeabedCoreEntity>> SEABED_CORE_ENTITY =
            BLOCK_ENTITIES.register("seabed_core_entity",
                    () -> BlockEntityType.Builder.of(SeabedCoreEntity::new, SEABED_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<SinkholeCoreEntity>> SINKHOLE_CORE_ENTITY =
            BLOCK_ENTITIES.register("sinkhole_core_entity",
                    () -> BlockEntityType.Builder.of(SinkholeCoreEntity::new, SINKHOLE_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TrawlCoreEntity>> TRAWL_CORE_ENTITY =
            BLOCK_ENTITIES.register("trawl_core_entity",
                    () -> BlockEntityType.Builder.of(TrawlCoreEntity::new, TRAWL_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<VortexCoreEntity>> VORTEX_CORE_ENTITY =
            BLOCK_ENTITIES.register("vortex_core_entity",
                    () -> BlockEntityType.Builder.of(VortexCoreEntity::new, VORTEX_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<WhirlpoolCoreEntity>> WHIRLPOOL_CORE_ENTITY =
            BLOCK_ENTITIES.register("whirlpool_core_entity",
                    () -> BlockEntityType.Builder.of(WhirlpoolCoreEntity::new, WHIRLPOOL_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<FlowUpgradeEntity>> FLOW_UPGRADE_ENTITY =
            BLOCK_ENTITIES.register("flow_upgrade_entity",
                    () -> BlockEntityType.Builder.of(FlowUpgradeEntity::new, FLOW_UPGRADE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BountyUpgradeEntity>> BOUNTY_UPGRADE_ENTITY =
            BLOCK_ENTITIES.register("bounty_upgrade_entity",
                    () -> BlockEntityType.Builder.of(BountyUpgradeEntity::new, BOUNTY_UPGRADE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RangeUpgradeEntity>> RANGE_UPGRADE_ENTITY =
            BLOCK_ENTITIES.register("range_upgrade_entity",
                    () -> BlockEntityType.Builder.of(RangeUpgradeEntity::new, RANGE_UPGRADE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ItemFilterEntity>> ITEM_FILTER_ENTITY =
            BLOCK_ENTITIES.register("item_filter_entity",
                    () -> BlockEntityType.Builder.of(ItemFilterEntity::new, ITEM_FILTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<FluidFilterEntity>> FLUID_FILTER_ENTITY =
            BLOCK_ENTITIES.register("fluid_filter_entity",
                    () -> BlockEntityType.Builder.of(FluidFilterEntity::new, FLUID_FILTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<EntityFilterEntity>> ENTITY_FILTER_ENTITY =
            BLOCK_ENTITIES.register("entity_filter_entity",
                    () -> BlockEntityType.Builder.of(EntityFilterEntity::new, ENTITY_FILTER.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
