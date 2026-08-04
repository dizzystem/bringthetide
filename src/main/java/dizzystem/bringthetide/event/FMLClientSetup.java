package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID, value = Dist.CLIENT)
public class FMLClientSetup {
    @SubscribeEvent
    public static void onFMLClientSetupEvent(FMLClientSetupEvent event){
        ItemBlockRenderTypes.setRenderLayer(TideFluids.IMBUED_SEAWATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(TideFluids.FLOWING_IMBUED_SEAWATER.get(), RenderType.translucent());

        event.enqueueWork(() -> {
            WoodType.register(TideBlocks.DRIFTWOOD_WOOD_TYPE);
            BlockSetType.register(TideBlocks.DRIFTWOOD_BLOCK_TYPE);
        });
    }
}
