package com.hronon.limitlesscrafting.gui.widgets;

import brachy.modularui.api.value.ISyncOrValue;
import brachy.modularui.api.value.IValue;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.GuiDraw;
import brachy.modularui.screen.viewport.ModularGuiContext;
import brachy.modularui.theme.WidgetThemeEntry;
import brachy.modularui.value.ObjectValue;
import brachy.modularui.widget.Widget;
import com.hronon.limitlesscrafting.blocks.Workbench;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class RecipeOutput extends Widget<RecipeOutput>
{
    private IValue<WorkbenchRecipe> recipe;

    public RecipeOutput()    {
    }

    public RecipeOutput recipe(IValue<WorkbenchRecipe> supplier)
    {
        setSyncOrValue(ISyncOrValue.orEmpty(supplier));
        return this;
    }

    public RecipeOutput recipe(WorkbenchRecipe recipe)
    {
        return recipe(new ObjectValue<>(WorkbenchRecipe.class, recipe));
    }

    @Override
    public boolean isValidSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        return syncOrValue.isValueOfType(WorkbenchRecipe.class);
    }

    @Override
    protected void setSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        super.setSyncOrValue(syncOrValue);
        this.recipe = syncOrValue.castValueNullable(WorkbenchRecipe.class);
    }

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry widgetTheme) {
        var recipe = this.recipe.getValue();

        GuiDraw.drawItem(context.getGraphics(), recipe.getResultItem(null), 0, 0, 16, 16 ,context.getCurrentDrawingZ());
    }
}
