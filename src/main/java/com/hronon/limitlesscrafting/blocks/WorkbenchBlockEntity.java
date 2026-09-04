package com.hronon.limitlesscrafting.blocks;

import brachy.modularui.api.IUIHolder;
import brachy.modularui.api.MCHelper;
import brachy.modularui.factory.GuiData;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;
import brachy.modularui.screen.UISettings;
import brachy.modularui.value.sync.PanelSyncManager;
import com.hronon.limitlesscrafting.LimitlessCraft;
import com.hronon.limitlesscrafting.gui.WorkbenchScreen;
import com.hronon.limitlesscrafting.registries.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class WorkbenchBlockEntity extends BlockEntity implements IUIHolder
{
    private final WorkbenchScreen screen;

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
        assert MCHelper.getPlayer() != null;
        this.screen = new WorkbenchScreen(MCHelper.getPlayer());
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

    @Override
    public ModularScreen createScreen(GuiData guiData, ModularPanel modularPanel) {
        return new ModularScreen(LimitlessCraft.MODID, modularPanel);
    }

    @Override
    public ModularPanel<?> buildUI(GuiData guiData, PanelSyncManager panelSyncManager, UISettings uiSettings)
    {
        return screen.get(guiData, panelSyncManager, uiSettings);
    }
}
