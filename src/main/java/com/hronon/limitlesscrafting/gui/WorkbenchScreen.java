package com.hronon.limitlesscrafting.gui;

import brachy.modularui.factory.GuiData;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.UISettings;
import brachy.modularui.value.sync.PanelSyncManager;
import brachy.modularui.widgets.*;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.layout.Grid;
import com.hronon.limitlesscrafting.blocks.Workbench;
import com.hronon.limitlesscrafting.blocks.WorkbenchBlockEntity;
import com.hronon.limitlesscrafting.gui.widgets.RecipeOutput;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import com.hronon.limitlesscrafting.registries.Items;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchScreen
{
    private Level level;

    public WorkbenchScreen(Level level)
    {
        this.level = level;
    }

    public ModularPanel<?> get(GuiData guiData, PanelSyncManager panelSyncManager, UISettings uiSettings)
    {
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
        var vertical_center_layout = Flow.col();
        vertical_center_layout.sizeRel(0.5f, 1f);

        var paged_widget = new PagedWidget();
        paged_widget.sizeRel(1f, 0.5f);
        paged_widget.top(0);

        var recipe_list_widget = new Grid();
        recipe_list_widget
                .full()
                .scrollable()
                .collapseDisabledChildren();

        for (WorkbenchRecipe recipe : getMatchingRecipes())
        {
            var recipe_widget = new RecipeOutput().recipe(recipe);
            recipe_list_widget.child(recipe_widget);
        }


        recipe_list_widget.child(new ItemDisplayWidget().item(Items.WORKBENCH.get().getDefaultInstance()));
        recipe_list_widget.child(new ButtonWidget());
        recipe_list_widget.child(new TextWidget(Component.translatable("test_string")));

        var player_inventory = SlotGroupWidget.playerInventory(true);
        player_inventory.heightRel(0.2f);

        paged_widget.addPage(recipe_list_widget);
        vertical_center_layout
                .child(paged_widget)
                .child(player_inventory);

        main_layout.child(vertical_center_layout);

        // right panel for recipe requirements and output\
        var right_panel = Flow.col();
        right_panel.widthRel(0.25f);
        main_layout.child(right_panel);


        ModularPanel panel = ModularPanel.defaultPanel("workbench_panel")
                .sizeRel(0.95f)
                .child(main_layout);

        return panel;
    }

    public List<WorkbenchRecipe> getMatchingRecipes()
    {
        var rm = this.level.getRecipeManager();
        return rm.getAllRecipesFor(WorkbenchRecipe.Type.INSTANCE);
    }
}
