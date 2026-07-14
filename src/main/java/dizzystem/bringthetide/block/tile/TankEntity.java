package dizzystem.bringthetide.block.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TankEntity extends BlockEntity implements IForgeBlockEntity {
    private final FluidTank fluid = createFluidHandler();
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);

    public static final String FLUIDS_TAG = "Tank";
    public static final int TANK_CAPACITY = 16000;

    public TankEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.TANK_ENTITY.get(), blockPos, blockState);
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

    public FluidStack getFluid(){
        return fluid.getFluid();
    }

    //This is used for both saving and updating clients with our data.
    protected void saveClientData(CompoundTag tag) {
        tag.put(FLUIDS_TAG, fluid.writeToNBT(new CompoundTag()));
    }

    //This is used for both saving and updating clients with our data.
    protected void loadClientData(CompoundTag tag) {
        if (tag.contains(FLUIDS_TAG)) {
            //load our tank
            fluid.readFromNBT(tag.getCompound(FLUIDS_TAG));
        }
    }

    //saving
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveClientData(tag);
    }

    //loading
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveClientData(tag);
        return tag;
    }

    //syncs to client
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //is synced from server
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){
        CompoundTag tag = pkt.getTag();

        if (tag != null){
            handleUpdateTag(tag);
        }
    }
}
