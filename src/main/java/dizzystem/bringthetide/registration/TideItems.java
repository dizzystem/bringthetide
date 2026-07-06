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
    public static final RegistryObject<Item> DRIFTWOOD_LOG_ITEM = ITEMS.register("driftwood_log",
            () -> new BlockItem(TideBlocks.DRIFTWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> CURRENT_CORE_ITEM = ITEMS.register("current_core",
            () -> new BlockItem(TideBlocks.CURRENT_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DEPOSITION_CORE_ITEM = ITEMS.register("deposition_core",
            () -> new BlockItem(TideBlocks.DEPOSITION_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> EROSION_CORE_ITEM = ITEMS.register("erosion_core",
            () -> new BlockItem(TideBlocks.EROSION_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SINKHOLE_CORE_ITEM = ITEMS.register("sinkhole_core",
            () -> new BlockItem(TideBlocks.SINKHOLE_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TRAWL_CORE_ITEM = ITEMS.register("trawl_core",
            () -> new BlockItem(TideBlocks.TRAWL_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> BASIN_CORE_ITEM = ITEMS.register("basin_core",
            () -> new BlockItem(TideBlocks.BASIN_CORE.get(), new Item.Properties()));
    public static final RegistryObject<Item> VORTEX_CORE_ITEM = ITEMS.register("vortex_core",
            () -> new BlockItem(TideBlocks.VORTEX_CORE.get(), new Item.Properties()));

//    public static final RegistryObject<Item> IMBUED_SEAWATER_BUCKET = ITEMS.register("imbued_seawater_bucket",
//            () -> new BucketItem(TideFluids.IMBUED_SEAWATER, new Item.Properties()));
    public static final RegistryObject<Item> DRIFTWOOD_WAND = ITEMS.register("driftwood_wand", DriftwoodWand::new);
    public static final RegistryObject<Item> CREATIVE_WAND = ITEMS.register("creative_wand", CreativeWand::new);
    public static final RegistryObject<Item> SEASHELL_ALLOY_INGOT = ITEMS.register("seashell_alloy_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MASCOT_HELMET = ITEMS.register("mascot_helmet",
            DolphinCostumeItem::new);

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
