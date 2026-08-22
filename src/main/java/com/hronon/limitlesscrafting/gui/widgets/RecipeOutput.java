package com.hronon.limitlesscrafting.gui.widgets;

import brachy.modularui.api.value.ISyncOrValue;
import brachy.modularui.api.value.IValue;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.GuiDraw;
import brachy.modularui.drawable.UITexture;
import brachy.modularui.screen.viewport.ModularGuiContext;
import brachy.modularui.theme.WidgetThemeEntry;
import brachy.modularui.utils.Color;
import brachy.modularui.value.ObjectValue;
import brachy.modularui.widget.Widget;
import brachy.modularui.widgets.ItemDisplayWidget;
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
    public static final UITexture BG_TEXTURE = UITexture.builder()
            .location(MODID, "textures/gui/recipe_output_background.png")
            .imageSize(32, 32)
            .build();

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

        var item = recipe.getResultItem(null);
        item.setCount(1);

        GuiDraw.drawItem(context.getGraphics(), item, 4, 2, 24, 24 ,context.getCurrentDrawingZ());

        var item_name = item.getDisplayName().getString();
        item_name = item_name.substring(1, item_name.length() - 1);
        var iw = new ItemDisplayWidget();
        iw.background();
        GuiDraw.drawText(context.getGraphics(), item_name, 32f + 16f, 2f, 1.5f, Color.argb(1f, 1f, 1f, 1f), true);
    }
}
