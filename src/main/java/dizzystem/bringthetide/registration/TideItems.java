package dizzystem.bringthetide.registration;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.item.CreativeWand;
import dizzystem.bringthetide.item.DolphinCostumeItem;
import dizzystem.bringthetide.item.DriftwoodWand;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TideItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            BringTheTide.MODID);

    public static final RegistryObject<Item> BUDDING_PRISMARINE_ITEM = ITEMS.register("budding_prismarine",
            () -> new BlockItem(TideBlocks.BUDDING_PRISMARINE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PRISMARINE_CLUSTER_ITEM = ITEMS.register("prismarine_cluster",
            () -> new BlockItem(TideBlocks.PRISMARINE_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> LARGE_PRISMARINE_BUD_ITEM = ITEMS.register("large_prismarine_bud",
            () -> new BlockItem(TideBlocks.LARGE_PRISMARINE_BUD.get(), new Item.Properties()));
    public static final RegistryObject<Item> MEDIUM_PRISMARINE_BUD_ITEM = ITEMS.register("medium_prismarine_bud",
            () -> new BlockItem(TideBlocks.MEDIUM_PRISMARINE_BUD.get(), new Item.Properties()));
    public static final RegistryObject<Item> SMALL_PRISMARINE_BUD_ITEM = ITEMS.register("small_prismarine_bud",
            () -> new BlockItem(TideBlocks.SMALL_PRISMARINE_BUD.get(), new Item.Properties()));

    public static final RegistryObject<Item> PRISMARINE_PILLAR_ITEM = ITEMS.register("prismarine_pillar",
            () -> new BlockItem(TideBlocks.PRISMARINE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_LOG_ITEM = ITEMS.register("driftwood_log",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_PLANKS_ITEM = ITEMS.register("driftwood_planks",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_STAIRS_ITEM = ITEMS.register("driftwood_stairs",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_SLAB_ITEM = ITEMS.register("driftwood_slab",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_FENCE_ITEM = ITEMS.register("driftwood_fence",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_FENCE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_FENCE_GATE_ITEM = ITEMS.register("driftwood_fence_gate",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_FENCE_GATE.get(), new Item.Properties()));
//    public static final RegistryObject<Item> DRIFTWOOD_SIGN_ITEM = ITEMS.register("driftwood_sign",
//            () -> new BlockItem(TideBlocks.DRIFTWOOD_SIGN.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_PRESSURE_PLATE_ITEM = ITEMS.register("driftwood_pressure_plate",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_BUTTON_ITEM = ITEMS.register("driftwood_button",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_BUTTON.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_COLUMN_ITEM = ITEMS.register("driftwood_column",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_COLUMN.get(), new Item.Properties()));

    public static final RegistryObject<Item> TANK_ITEM = ITEMS.register("fluid_tank",
            () -> new BlockItem(TideBlocks.TANK.get(), new Item.Properties()));
    public static final RegistryObject<Item> EXPLOSION_ROD = ITEMS.register("explosion_rod",
            () -> new BlockItem(TideBlocks.EXPLOSION_ROD.get(), new Item.Properties()));

    public static final RegistryObject<Item> CURRENT_CORE_ITEM = ITEMS.register("current_core",
            () -> new BlockItem(TideBlocks.CURRENT_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEPOSITION_CORE_ITEM = ITEMS.register("deposition_core",
            () -> new BlockItem(TideBlocks.DEPOSITION_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> EROSION_CORE_ITEM = ITEMS.register("erosion_core",
            () -> new BlockItem(TideBlocks.EROSION_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> REEF_CORE_ITEM = ITEMS.register("reef_core",
            () -> new BlockItem(TideBlocks.REEF_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> PELAGIC_CORE_ITEM = ITEMS.register("pelagic_core",
            () -> new BlockItem(TideBlocks.PELAGIC_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SINKHOLE_CORE_ITEM = ITEMS.register("sinkhole_core",
            () -> new BlockItem(TideBlocks.SINKHOLE_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SEABED_CORE_ITEM = ITEMS.register("seabed_core",
            () -> new BlockItem(TideBlocks.SEABED_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TRAWL_CORE_ITEM = ITEMS.register("trawl_core",
            () -> new BlockItem(TideBlocks.TRAWL_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> BASIN_CORE_ITEM = ITEMS.register("basin_core",
            () -> new BlockItem(TideBlocks.BASIN_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> VORTEX_CORE_ITEM = ITEMS.register("vortex_core",
            () -> new BlockItem(TideBlocks.VORTEX_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> WHIRLPOOL_CORE_ITEM = ITEMS.register("whirlpool_core",
            () -> new BlockItem(TideBlocks.WHIRLPOOL_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> FLOW_UPGRADE_ITEM = ITEMS.register("flow_upgrade",
            () -> new BlockItem(TideBlocks.FLOW_UPGRADE.get(), new Item.Properties()));
    public static final RegistryObject<Item> BOUNTY_UPGRADE_ITEM = ITEMS.register("bounty_upgrade",
            () -> new BlockItem(TideBlocks.BOUNTY_UPGRADE.get(), new Item.Properties()));
    public static final RegistryObject<Item> RANGE_UPGRADE_ITEM = ITEMS.register("range_upgrade",
            () -> new BlockItem(TideBlocks.RANGE_UPGRADE.get(), new Item.Properties()));
    public static final RegistryObject<Item> ITEM_FILTER_ITEM = ITEMS.register("item_filter",
            () -> new BlockItem(TideBlocks.ITEM_FILTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> FLUID_FILTER_ITEM = ITEMS.register("fluid_filter",
            () -> new BlockItem(TideBlocks.FLUID_FILTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENTITY_FILTER_ITEM = ITEMS.register("entity_filter",
            () -> new BlockItem(TideBlocks.ENTITY_FILTER.get(), new Item.Properties()));

//    public static final RegistryObject<Item> IMBUED_SEAWATER_BUCKET = ITEMS.register("imbued_seawater_bucket",
//            () -> new BucketItem(TideFluids.IMBUED_SEAWATER, new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_WAND = ITEMS.register("driftwood_wand", DriftwoodWand::new);
    public static final RegistryObject<Item> CREATIVE_WAND = ITEMS.register("creative_wand", CreativeWand::new);
    public static final RegistryObject<Item> TURTLE_ALLOY_INGOT = ITEMS.register("turtle_alloy_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FISH_ALLOY_INGOT = ITEMS.register("fish_alloy_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SEASHELL_ALLOY_INGOT = ITEMS.register("seashell_alloy_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SEABOUND_SKIN = ITEMS.register("seabound_skin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COSTUME_HELMET = ITEMS.register("costume_helmet",
            () -> new DolphinCostumeItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> COSTUME_CHESTPLATE = ITEMS.register("costume_chestplate",
            () -> new DolphinCostumeItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> COSTUME_LEGGINGS = ITEMS.register("costume_leggings",
            () -> new DolphinCostumeItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> COSTUME_BOOTS = ITEMS.register("costume_boots",
            () -> new DolphinCostumeItem(ArmorItem.Type.BOOTS));

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
