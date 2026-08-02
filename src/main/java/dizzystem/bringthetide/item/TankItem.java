package dizzystem.bringthetide.item;

import dizzystem.bringthetide.block.tile.TankEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TankItem extends BlockItem {

    public TankItem(Block block, Item.Properties props){
        super(block, props);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidCapabilityProvider(stack, nbt);
    }

    public class FluidCapabilityProvider implements ICapabilityProvider {
        private final ItemStack item;
        private final FluidHandlerItemStack fluid;
        private final LazyOptional<IFluidHandlerItem> fluidHandler;

        private final String BLOCK_ENTITY_TAG = "BlockEntityTag";

        public FluidCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt){
            FluidCapabilityProvider capabilityProvider = this;

            this.item = stack;
            this.fluid = new FluidHandlerItemStack(stack, TankEntity.TANK_CAPACITY){
                @Override
                protected void setFluid(FluidStack fluid) {
                    super.setFluid(fluid);
                    capabilityProvider.onContentsChanged();
                }

                @Override
                protected void setContainerToEmpty() {
                    super.setContainerToEmpty();
                    capabilityProvider.onContentsChanged();
                }
            };
            this.fluidHandler = LazyOptional.of(() -> this.fluid);

            writeFluidToItemStackData(readFluidFromBlockEntityData());
        }

        private FluidStack readFluidFromBlockEntityData(){
            CompoundTag tag = this.item.getOrCreateTag();
            if (!tag.contains(BLOCK_ENTITY_TAG)) {
                return FluidStack.EMPTY;
            }

            CompoundTag entityData = tag.getCompound(BLOCK_ENTITY_TAG);

            if (!entityData.contains(TankEntity.FLUIDS_TAG)){
                return FluidStack.EMPTY;
            }

            return FluidStack.loadFluidStackFromNBT(entityData.getCompound(TankEntity.FLUIDS_TAG));
        }
        private void writeFluidToBlockEntityData(FluidStack fluidStack){
            CompoundTag tag = this.item.getOrCreateTag();
            if (!tag.contains(BLOCK_ENTITY_TAG)){
                tag.put(BLOCK_ENTITY_TAG, new CompoundTag());
            }

            CompoundTag beTag = tag.getCompound(BLOCK_ENTITY_TAG);
            if (fluidStack.isEmpty()){
                beTag.remove(TankEntity.FLUIDS_TAG);
                if (beTag.isEmpty()){
                    tag.remove(BLOCK_ENTITY_TAG);
                    if (tag.isEmpty()){
                        tag = null;
                    }
                }
            } else {
                beTag.put(TankEntity.FLUIDS_TAG, fluidStack.writeToNBT(new CompoundTag()));
            }

            this.item.setTag(tag);
        }
        private FluidStack readFluidFromItemStackData(){
            CompoundTag tag = this.item.getOrCreateTag();
            if (!tag.contains(FluidHandlerItemStack.FLUID_NBT_KEY)) {
                return FluidStack.EMPTY;
            }

            return FluidStack.loadFluidStackFromNBT(tag.getCompound(FluidHandlerItemStack.FLUID_NBT_KEY));
        }
        private void writeFluidToItemStackData(FluidStack fluidStack){
            CompoundTag tag = this.item.getOrCreateTag();

            tag.put(FluidHandlerItemStack.FLUID_NBT_KEY, fluidStack.writeToNBT(new CompoundTag()));

            this.item.setTag(tag);
        }

        //If our contents change, write the change back to the NBT on the itemStack.
        public void onContentsChanged(){
            writeFluidToBlockEntityData(readFluidFromItemStackData());
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side){
            if (capability == ForgeCapabilities.FLUID_HANDLER_ITEM){
                return fluidHandler.cast();
            } else {
                return LazyOptional.empty();
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(item, level, tooltip, flag);

        LazyOptional<IFluidHandlerItem> fluidHandler = item.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        if (fluidHandler.isPresent()){
            FluidStack fluid = fluidHandler.orElse(null).getFluidInTank(0);
            if (!fluid.isEmpty()){
                tooltip.add(fluid.getDisplayName().copy().append(": ").append(((Integer) fluid.getAmount()).toString())
                        .withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.ITALIC));
            }
        }
    }
}
