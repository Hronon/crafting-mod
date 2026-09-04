package com.hronon.limitlesscrafting.gui;

import brachy.modularui.factory.GuiData;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.UISettings;
import brachy.modularui.utils.Alignment;
import brachy.modularui.value.sync.PanelSyncManager;
import brachy.modularui.widgets.*;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.layout.Grid;
import com.hronon.limitlesscrafting.gui.widgets.CustomItemDisplayWidget;
import com.hronon.limitlesscrafting.gui.widgets.PredefinedWidgets;
import com.hronon.limitlesscrafting.gui.widgets.RecipeOutput;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkbenchScreen
{
    private Level level;
    private Player player;

    private List<WorkbenchRecipe> availableRecipes = new ArrayList<>(1);
    private Map<Item, Integer> availableItems = new HashMap<>();

    private Optional<WorkbenchRecipe> selectedRecipe = Optional.empty();
    private int craftAmount = 0;
    private int amountCanCraft = 0;

    public WorkbenchScreen(Player player)
    {
        this.player = player;
        this.level = player.level();
        this.availableRecipes = getAvailableRecipes();
    }

    public ModularPanel<?> get(GuiData guiData, PanelSyncManager panelSyncManager, UISettings uiSettings)
    {
        uiSettings.getRecipeViewerSettings().disable();

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

        for (WorkbenchRecipe recipe : getAvailableRecipes())
        {
            var flow = Flow.row()
                    .widthRel(1f)
                    .height(34 + 16)
                    .mainAxisAlignment(Alignment.MainAxis.START)
                    .crossAxisAlignment(Alignment.CrossAxis.START);

            var item = recipe.getResultItem(null);
            var amount = Integer.toString(item.getCount());
            item.setCount(1);

            // get item name and remove brackets
            var item_name = item.getDisplayName().getString();
            item_name = item_name.substring(1, item_name.length() - 1).concat(" x" + amount);

            var recipe_widget = new CustomItemDisplayWidget()
                    .item(item)
                    .background(RecipeOutput.BG_TEXTURE)
                    .size(34);

            flow.child(recipe_widget);

            var text_layout = Flow.col()
                    .height(34)
                    .mainAxisAlignment(Alignment.MainAxis.START)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .padding(10);
            flow.child(text_layout);

            text_layout.child(new TextWidget(item_name));

            var requirements = PredefinedWidgets.recipe_requirement_list(recipe);
            requirements.full();

            left_panel_layout.child(requirements);
        }

        var player_inventory = SlotGroupWidget.playerInventory(true);
        player_inventory.heightRel(0.2f);

        vertical_center_layout
                .child(player_inventory);

        main_layout.child(vertical_center_layout);

        // right panel for recipe requirements and output\
        var right_panel = Flow.col();
        right_panel.widthRel(0.25f);

        var max_button = new ButtonWidget<>()
                .onMousePressed((context, button) -> {
                    if (button == 0 || button == 1)
                    {
                        selectRecipe(0);
                        setMaxAmount();
                        craft();
                        return true;
                    }

                    return false;
                });
        max_button.size(50);
        right_panel.child(max_button);

        main_layout.child(right_panel);


        ModularPanel panel = ModularPanel.defaultPanel("workbench_panel")
                .sizeRel(0.95f)
                .padding(10)
                .child(main_layout);

        return panel;
    }

    public void setCraftAmount(int amount)
    {
        if (selectedRecipe.isEmpty())
            return;

        craftAmount = Math.max(0, Math.min(amount, amountCanCraft));
    }

    public void setMaxAmount()
    {
        setCraftAmount(amountCanCraft);
    }

    public void craft()
    {
        if (selectedRecipe.isEmpty())
            return;

        var recipe = selectedRecipe.get();
        var inv = player.getInventory();
        var result_item = selectedRecipe.get().getResultItem(null);

        recipe.inputs().forEach(stack ->
            takeItemsFromInventory(inv, stack.getItem(), stack.getCount() * craftAmount)
        );

        putItemsToInventory(inv, result_item.getItem(), craftAmount * result_item.getCount());
        inv.setChanged();
    }

    private void takeItemsFromInventory(Inventory inv, Item item, int amount)
    {
        int remain = amount;

        for (ItemStack stack : inv.items)
        {
            if (!stack.isEmpty() && stack.is(item) && remain > 0)
            {
                int to_remove = Math.min(stack.getCount(), remain);
                stack.shrink(to_remove);
                remain -= to_remove;
            }
        }
    }

    private void putItemsToInventory(Inventory inv, Item item, int amount)
    {
        int remain = amount;

        while (remain > 0)
        {
            int to_add = Math.min(item.getMaxStackSize(), remain);
            inv.add(new ItemStack(item, to_add));
            remain -= to_add;
        }
    }

    public Map<Item, Integer> getAvailableItems(WorkbenchRecipe recipe)
    {
        Map<Item, Integer> totals = new HashMap<>();

        recipe.inputs()
            .stream()
            .map(ItemStack::getItem)
            .forEach((item) -> {

                totals.put(item, 0);

                player.getInventory().items.stream()
                        .filter(stack ->
                                stack.getItem() == item
                        )
                        .forEach(stack ->
                                totals.merge(
                                    stack.getItem(),
                                    stack.getCount(),
                                    Integer::sum
                                )
                        );
            });

        return totals;
    }

    public void selectRecipe(int index)
    {
        if (index >= 0 && index < availableRecipes.size())
        {
            selectRecipe(availableRecipes.get(index));
        }
    }

    private int findMaxCraftAmount(WorkbenchRecipe recipe, Map<Item, Integer> available)
    {
        AtomicInteger max = new AtomicInteger(Integer.MAX_VALUE);

        recipe.inputs()
                .forEach(stack -> {
                    var have = available.get(stack.getItem());
                    max.set(Math.min(have / stack.getCount(), max.get()));
                });

        return max.get();
    }

    private void selectRecipe(WorkbenchRecipe recipe)
    {
        availableItems = getAvailableItems(recipe);
        amountCanCraft = findMaxCraftAmount(recipe, availableItems);
        craftAmount = 0;

        selectedRecipe = Optional.of(recipe);
    }

    private List<WorkbenchRecipe> getAvailableRecipes()
    {
        return this.level.getRecipeManager()
                .getAllRecipesFor(WorkbenchRecipe.Type.INSTANCE);
    }
}
