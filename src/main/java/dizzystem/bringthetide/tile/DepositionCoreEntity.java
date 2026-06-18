package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideRecipes;
import dizzystem.bringthetide.util.PoolHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.util.Map.entry;

public class DepositionCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, -1), Blocks.PRISMARINE),
            entry(new Vec3i(-1, 0, -1), Blocks.PRISMARINE)
    );
    public static final String ITEMS_TAG = "Inventory";
    public static final int SLOT_COUNT = 1;
    public static final int SLOT = 0;

    private final ItemStackHandler items = createItemHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

    public DepositionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.DEPOSITION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos){
        if (!(entity instanceof ItemEntity itemEntity)) {
            return;
        }

        PoolHandler.attemptCraft(itemEntity, level, pos, TideRecipes.DEPOSITION.get());
    }

    //this removes the capability if the block is broken
    @Override
    public void invalidateCaps(){
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Nonnull
    private ItemStackHandler createItemHandler(){
        return new ItemStackHandler(SLOT_COUNT){
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

    public void setItemStack(ItemStack itemStack){
        items.setStackInSlot(0, itemStack);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
