package com.hronon.limitlesscrafting.blocks;

import brachy.modularui.api.GuiAxis;
import brachy.modularui.api.IUIHolder;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.schema.ISchema;
import brachy.modularui.drawable.schema.SchemaLevel;
import brachy.modularui.factory.GuiData;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;
import brachy.modularui.screen.UISettings;
import brachy.modularui.value.sync.PanelSyncManager;
import brachy.modularui.widgets.*;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.layout.Grid;
import com.hronon.limitlesscrafting.LimitlessCraft;
import com.hronon.limitlesscrafting.gui.WorkbenchMenu;
import com.hronon.limitlesscrafting.registries.BlockEntities;
import com.hronon.limitlesscrafting.registries.Blocks;
import com.hronon.limitlesscrafting.registries.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
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

import javax.swing.*;
import java.util.List;

public class WorkbenchBlockEntity extends BlockEntity implements IUIHolder {
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
    public ModularPanel<?> buildUI(GuiData guiData, PanelSyncManager panelSyncManager, UISettings uiSettings) {
        uiSettings.getRecipeViewerSettings().disable();
        uiSettings.useTheme("Create");

        // main layout
        var main_layout = Flow.row();
        main_layout.full();

        // left panel for item info
        var left_panel_layout = Flow.col();
        left_panel_layout.sizeRel(0.25f, 1f);
        main_layout.child(left_panel_layout);

        // center panel for recipe list and preview
        var paged_widget = new PagedWidget();
        paged_widget.sizeRel(0.5f, 0.8f);
        paged_widget.top(0);

        var recipe_list_widget = new Grid();
        recipe_list_widget
                .full()
                .scrollable()
                .collapseDisabledChildren();

        recipe_list_widget.child(new ItemDisplayWidget().item(Items.WORKBENCH.get().getDefaultInstance()));
        recipe_list_widget.child(new ButtonWidget());
        recipe_list_widget.child(new TextWidget(Component.translatable("test_string")));

        paged_widget.addPage(recipe_list_widget);
        main_layout.child(paged_widget);

        // right panel for recipe requirements and output\
        var right_panel = Flow.col();
        right_panel.widthRel(0.25f);
        main_layout.child(right_panel);


        ModularPanel panel = ModularPanel.defaultPanel("workbench_panel")
                .sizeRel(0.95f)
                .bindPlayerInventory()
                .child(main_layout);
        
        return panel;
    }
}
