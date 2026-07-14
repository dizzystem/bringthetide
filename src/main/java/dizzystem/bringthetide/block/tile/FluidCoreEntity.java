package dizzystem.bringthetide.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class FluidCoreEntity extends CoreEntity {
    private final FluidTank fluid = createFluidHandler();
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);

    public static final String FLUIDS_TAG = "Tank";
    public static final int TANK_CAPACITY = 1000;

    public FluidCoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    //this removes the capability if the block is broken
    @Override
    public void invalidateCaps(){
        super.invalidateCaps();
        fluidHandler.invalidate();
    }

    @Nonnull
    private FluidTank createFluidHandler(){
        return new FluidTank(TANK_CAPACITY);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side){
        if (capability == ForgeCapabilities.FLUID_HANDLER){
            return fluidHandler.cast();
        } else {
            return super.getCapability(capability, side);
        }
    }

    public void setFluid(FluidStack fluidStack){
        fluid.setFluid(fluidStack);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public FluidStack getFluid(){
        return fluid.getFluid();
    }

    public LazyOptional<IFluidHandler> getFluidHandler(){
        return fluidHandler;
    }

    @Override
    protected void saveClientData(CompoundTag tag) {
        super.saveClientData(tag);
        //save our tank
        tag.put(FLUIDS_TAG, fluid.writeToNBT(new CompoundTag()));
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        super.loadClientData(tag);
        if (tag.contains(FLUIDS_TAG)) {
            //load our tank
            fluid.readFromNBT(tag.getCompound(FLUIDS_TAG));
        }
    }
}
