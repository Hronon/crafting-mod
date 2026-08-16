package com.hronon.limitlesscrafting.gui;

import com.hronon.limitlesscrafting.blocks.WorkbenchBlockEntity;
import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import com.hronon.limitlesscrafting.registries.Blocks;
import com.hronon.limitlesscrafting.registries.Menus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class WorkbenchMenu
    extends AbstractContainerMenu
{
    public final WorkbenchBlockEntity be;
    private final Level level;

    public WorkbenchMenu(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public WorkbenchMenu(int id, Inventory inv, BlockEntity be)
    {
        super(Menus.WORKBENCH.get(), id);
        this.be = (WorkbenchBlockEntity) be;
        this.level = inv.player.level();

        addPlayerInventory(inv);

        this.be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(item_handler -> {
            this.addSlot(new SlotItemHandler(item_handler, 0, 100, 11));
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int id) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(
                ContainerLevelAccess.create(
                        level,
                        be.getBlockPos()
                ),
                player,
                Blocks.WORKBENCH.get()
        );
    }

    private void addPlayerInventory(Inventory inv)
    {
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slot_idx = col + row * 9 + 9;
                this.addSlot(new Slot(inv, slot_idx, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++)
        {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }

    public List<WorkbenchRecipe> getMatchingRecipes()
    {
        var rm = level.getRecipeManager();
        return rm.getAllRecipesFor(WorkbenchRecipe.Type.INSTANCE);
    }
}
