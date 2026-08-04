package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.util.BlockEntityTypeAccessor;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            Sheets.addWoodType(TideBlocks.DRIFTWOOD_WOOD_TYPE);
            BlockEntityType<SignBlockEntity> signType = BlockEntityType.SIGN;
            if (signType instanceof BlockEntityTypeAccessor signTypeAccessor){
                signTypeAccessor.addTideBlock(TideBlocks.DRIFTWOOD_SIGN.get());
                signTypeAccessor.addTideBlock(TideBlocks.DRIFTWOOD_WALL_SIGN.get());
            }
        });

//        LOGGER.info("HELLO FROM COMMON SETUP");
//
//        if (Config.logDirtBlock)
//            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
//
//        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }
}
