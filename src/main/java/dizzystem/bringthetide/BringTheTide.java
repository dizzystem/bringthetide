package dizzystem.bringthetide;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.datagen.TideDataGenerator;
import dizzystem.bringthetide.registration.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BringTheTide.MODID)
public class BringTheTide {
    public static final String MODID = "bringthetide";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BringTheTide(FMLJavaModLoadingContext context){
        IEventBus modEventBus = context.getModEventBus();
        Registration.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Registration::addCreative);
        modEventBus.addListener(Registration::addFluidsClient);
        modEventBus.addListener(TideDataGenerator::generate);

        //Tutorial doesn't have this. What does it do?
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }
}
