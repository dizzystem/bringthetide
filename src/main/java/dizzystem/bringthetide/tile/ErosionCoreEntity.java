package dizzystem.bringthetide.tile;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

public class ErosionCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(-1, 0, 0), Blocks.SANDSTONE),
            entry(new Vec3i(1, 0, -1), Blocks.SANDSTONE),
            entry(new Vec3i(-1, 0, -1), Blocks.SANDSTONE)
    );

    private final FluidTank fluid = createFluidHandler();
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> fluid);

    public static final String FLUIDS_TAG = "Tank";
    public static final int TANK_CAPACITY = 1000;

    public ErosionCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.EROSION_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //called when an entity enters our pool
    public void entityInPool(Entity entity, Level level, BlockPos pos){
        if (!(entity instanceof ItemEntity)) {
            return;
        }
        if (!this.isPoolActive()){
            return;
        }

        ItemStack itemStack = ((ItemEntity) entity).getItem();
        IItemHandlerModifiable inputs = new ItemStackHandler(1);
        inputs.setStackInSlot(0, itemStack);
        RecipeWrapper inputWrapper = new RecipeWrapper(inputs);

        Optional<ErosionRecipe> maybeRecipe =
                this.level.getRecipeManager().getRecipeFor(
                        TideRecipes.EROSION.get(),
                        inputWrapper,
                        level);
        ErosionRecipe recipe;
        if (!maybeRecipe.isPresent()) {
            return;
        }

        recipe = maybeRecipe.get();
        itemStack.split(1);

        ItemStack output = recipe.assemble(inputWrapper, level.registryAccess());
        ItemEntity outputEntity = new ItemEntity(level, pos.getX()+0.5,
                this.getBlockPos().getY() + 1, pos.getZ()+0.5, output);
        outputEntity.setDeltaMovement(0, 0, 0);
        outputEntity.setNoGravity(true);
        outputEntity.setPickUpDelay(20);
        level.addFreshEntity(outputEntity);

        ((ServerLevel) level).sendParticles(TideParticles.SPLASH.get(),
                pos.getX() + .5,
                pos.getY() + 1.5,
                pos.getZ() + .5,
                10,
                0,
                0,
                0,
                0.25);
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
