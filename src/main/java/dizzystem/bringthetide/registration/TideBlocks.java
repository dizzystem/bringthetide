package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.block.*;
import dizzystem.bringthetide.block.tile.*;
import dizzystem.bringthetide.fluid.BlockImbuedSeawater;
import net.minecraft.world.level.block.*;
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
    public static final RegistryObject<Block> TANK = BLOCKS.register("fluid_tank",
            Tank::new);
    public static final RegistryObject<Block> OCEANIFIED_TNT = BLOCKS.register("oceanified_tnt",
            () -> new Block(BlockBehaviour.Properties.of()));

    public static final RegistryObject<Block> BASIN_CORE = BLOCKS.register("basin_core",
            BasinCore::new);
    public static final RegistryObject<Block> CURRENT_CORE = BLOCKS.register("current_core",
            CurrentCore::new);
    public static final RegistryObject<Block> DEPOSITION_CORE = BLOCKS.register("deposition_core",
            DepositionCore::new);
    public static final RegistryObject<Block> EROSION_CORE = BLOCKS.register("erosion_core",
            ErosionCore::new);
    public static final RegistryObject<Block> PELAGIC_CORE = BLOCKS.register("pelagic_core",
            PelagicCore::new);
    public static final RegistryObject<Block> REEF_CORE = BLOCKS.register("reef_core",
            ReefCore::new);
    public static final RegistryObject<Block> SINKHOLE_CORE = BLOCKS.register("sinkhole_core",
            SinkholeCore::new);
    public static final RegistryObject<Block> TRAWL_CORE = BLOCKS.register("trawl_core",
            TrawlCore::new);
    public static final RegistryObject<Block> VORTEX_CORE = BLOCKS.register("vortex_core",
            VortexCore::new);
    public static final RegistryObject<Block> WHIRLPOOL_CORE = BLOCKS.register("whirlpool_core",
            WhirlpoolCore::new);
    public static final RegistryObject<Block> FLOW_UPGRADE = BLOCKS.register("flow_upgrade",
            FlowUpgrade::new);
    public static final RegistryObject<Block> BOUNTY_UPGRADE = BLOCKS.register("bounty_upgrade",
            BountyUpgrade::new);
    public static final RegistryObject<Block> RANGE_UPGRADE = BLOCKS.register("range_upgrade",
            RangeUpgrade::new);
    public static final RegistryObject<Block> ITEM_FILTER = BLOCKS.register("item_filter",
            ItemFilter::new);
    public static final RegistryObject<Block> FLUID_FILTER = BLOCKS.register("fluid_filter",
            FluidFilter::new);
    public static final RegistryObject<Block> ENTITY_FILTER = BLOCKS.register("entity_filter",
            EntityFilter::new);
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
