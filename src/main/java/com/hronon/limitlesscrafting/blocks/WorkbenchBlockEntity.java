package com.hronon.limitlesscrafting.blocks;

import com.hronon.limitlesscrafting.gui.WorkbenchMenu;
import com.hronon.limitlesscrafting.registries.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class WorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler block_inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }
    };
    private final int OUTPUT_SLOT = 0;

    private LazyOptional<IItemHandler> lazy_item_handler = LazyOptional.empty();


    public WorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.WORKBENCH.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("My Screen");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new WorkbenchMenu(id, inv, this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazy_item_handler = LazyOptional.of(() -> block_inventory);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
        {
            return lazy_item_handler.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazy_item_handler.invalidate();
    }

    public void drops()
    {
        // copy output slot stack
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(OUTPUT_SLOT, block_inventory.getStackInSlot(OUTPUT_SLOT));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put("workbench.inventory", block_inventory.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        block_inventory.deserializeNBT(tag.getCompound("workbench.inventory"));
    }
}
