package dizzystem.bringthetide.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public abstract class ItemCoreEntity extends CoreEntity {
    public static final String ITEMS_TAG = "Inventory";
    private final ItemStackHandler items = createItemHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

    public ItemCoreEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState){
        super(blockEntityType, blockPos, blockState);
    }

    //this removes the capability if the block is broken
    @Override
    public void invalidateCaps(){
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Nonnull
    private ItemStackHandler createItemHandler(){
        return new ItemStackHandler(1){
            @Override
            public int getSlotLimit(int slot){
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot){
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side){
        if (capability == ForgeCapabilities.ITEM_HANDLER){
            return itemHandler.cast();
        } else {
            return super.getCapability(capability, side);
        }
    }

    public ItemStack getItemStack(){
        return items.getStackInSlot(0);
    }

    @Override
    protected void saveClientData(CompoundTag tag) {
        super.saveClientData(tag);
        //save our item
        tag.put(ITEMS_TAG, items.serializeNBT());
    }

    @Override
    protected void loadClientData(CompoundTag tag) {
        super.loadClientData(tag);
        if (tag.contains(ITEMS_TAG)) {
            //load our item
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
    }
}
