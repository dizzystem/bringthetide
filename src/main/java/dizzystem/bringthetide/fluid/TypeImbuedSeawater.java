package dizzystem.bringthetide.fluid;

import dizzystem.bringthetide.BringTheTide;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class TypeImbuedSeawater extends FluidType {
    public TypeImbuedSeawater(){
        super(FluidType.Properties.create()
                        .descriptionId("type_imbued_seawater")
                        .canExtinguish(true)
                        .supportsBoating(true)
                        .canHydrate(true)
                        .lightLevel(9)
                        .canConvertToSource(true)
                );
    }

    //Sets the textures for the fluid. This has to be done in the FluidType file.
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer){
        consumer.accept(new IClientFluidTypeExtensions(){
            private static final ResourceLocation
                    STILL = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/fluids/imbued_seawater"),
                    FLOW = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "block/fluids/imbued_seawater_flowing");

            @Override
            public ResourceLocation getStillTexture(){
                return STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture(){
                return FLOW;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos)
            {
                return 0xFF33FFCC;
            }
        });
    }
}
