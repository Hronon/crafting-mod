package com.hronon.limitlesscrafting.gui;

import com.hronon.limitlesscrafting.gui.widgets.RecipeOutput;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class WorkbenchScreen
        extends AbstractContainerScreen<WorkbenchMenu>
{
    private List<WorkbenchRecipe> recipes = new ArrayList<>();

    public WorkbenchScreen(WorkbenchMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init()
    {
        this.imageWidth = 10000;
        this.imageHeight = 10000;
        this.width = 10000;
        this.height = 10000;
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        super.init();
        recipes = menu.getMatchingRecipes();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partial_tick, int mouse_x, int mouse_y) {
        int x = 5;
        int y = 5;

        this.addRenderableWidget(new RecipeOutput(x, y, 64, 16, () -> recipes.get(0).getResultItem(null)));
    }

    @Override
    public void render(GuiGraphics graphics, int mouse_x, int mouse_y, float delta)
    {
        renderBackground(graphics);
        super.render(graphics, mouse_x, mouse_y, delta);
        renderTooltip(graphics, mouse_x, mouse_y);
    }
}
