package com.hronon.limitlesscrafting.gui.widgets;

import brachy.modularui.api.value.ISyncOrValue;
import brachy.modularui.api.value.IValue;
import brachy.modularui.api.widget.ISynced;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.utils.Color;
import brachy.modularui.value.ObjectValue;
import brachy.modularui.value.sync.ModularSyncManager;
import brachy.modularui.value.sync.SyncHandler;
import brachy.modularui.widget.Widget;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.layout.Flow;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PredefinedWidgets
{

    public static RecipeRequirementDisplay recipe_requirement(ItemStack req)
    {
        return new RecipeRequirementDisplay().require(req);
    }

    public static class RecipeRequirementDisplay
            extends Widget<RecipeRequirementDisplay>
    {
        private IValue<ItemStack> required_item;

        @Override
        public boolean isValidSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
            return syncOrValue.isValueOfType(ItemStack.class);
        }

        @Override
        protected void setSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
            super.setSyncOrValue(syncOrValue);
            this.required_item = syncOrValue.castValueNullable(ItemStack.class);
        }

        public RecipeRequirementDisplay require(IValue<ItemStack> supplier)
        {
            setSyncOrValue(ISyncOrValue.orEmpty(supplier));
            return this;
        }

        public RecipeRequirementDisplay require(ItemStack req)
        {
            return require(new ObjectValue<>(ItemStack.class, req));
        }

        public IWidget get()
        {
            var flow = Flow.row()
                    .fullWidth()
                    .height(18);

            var item = this.required_item.getValue();
            var item_name = item.getDisplayName().getString();
            item_name = item_name.substring(1, item_name.length() - 1);
            var req_amount = item.getCount();
            var amount = 0;

            item.setCount(1);

            var item_widget = new CustomItemDisplayWidget()
                    .item(item)
                    .background(RecipeOutput.BG_TEXTURE)
                    .size(18);

            flow.child(item_widget);

            var display_text = item_name
                    + " ["
                    + amount
                    + "/"
                    + req_amount
                    + "]";

            var display_color = Color.rgb(213, 32, 39);
            if (amount >= req_amount)
            {
                display_color = Color.rgb(7, 177, 81);
            }

            var text_widget = new TextWidget<>(display_text)
                    .color(display_color);

            flow.child(text_widget);

            return flow;
        }
    }
}
