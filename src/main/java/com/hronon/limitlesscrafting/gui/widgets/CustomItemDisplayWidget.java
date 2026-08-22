package com.hronon.limitlesscrafting.gui.widgets;

import brachy.modularui.api.GuiAxis;
import brachy.modularui.api.value.ISyncOrValue;
import brachy.modularui.api.value.IValue;
import brachy.modularui.drawable.GuiDraw;
import brachy.modularui.integration.recipeviewer.RecipeSlotRole;
import brachy.modularui.screen.viewport.ModularGuiContext;
import brachy.modularui.theme.WidgetThemeEntry;
import brachy.modularui.value.ObjectValue;
import brachy.modularui.widgets.ItemDisplayWidget;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

public class CustomItemDisplayWidget extends ItemDisplayWidget
{
    private IValue<ItemStack> value;
    private boolean displayAmount = false;
    private final int ITEM_SIZE = 16;

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        ItemStack item = value.getValue();

        if (!item.isEmpty()) {
            var x = getArea().getSize(GuiAxis.X);
            var y = getArea().getSize(GuiAxis.Y);
            float x_dif = (x - 2) / (float) ITEM_SIZE;
            float y_dif = (y - 2) / (float) ITEM_SIZE;

            GuiDraw.drawItem(context.getGraphics(), item, (int) (2 * x_dif), (int) (1.5 * y_dif),
                     x - 4 * x_dif,
                    y - 4 * y_dif,
                    context.getCurrentDrawingZ()
            );
            if (this.displayAmount) {
                GuiDraw.drawStandardSlotAmountText(context, item.getCount(), null, getArea(), 0);
            }
        }
    }

    @Override
    public boolean isValidSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        return syncOrValue.isValueOfType(ItemStack.class);
    }

    @Override
    protected void setSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        super.setSyncOrValue(syncOrValue);
        this.value = syncOrValue.castValueNullable(ItemStack.class);
    }

    public CustomItemDisplayWidget item(IValue<ItemStack> itemSupplier) {
        setSyncOrValue(ISyncOrValue.orEmpty(itemSupplier));
        return this;
    }

    public CustomItemDisplayWidget item(ItemStack itemStack) {
        return item(new ObjectValue<>(ItemStack.class, itemStack));
    }
}
