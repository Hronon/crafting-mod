package com.hronon.limitlesscrafting.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class RecipeOutput extends AbstractWidget
{
    private static final ResourceLocation BG =
            new ResourceLocation(MODID, "textures/gui/recipe_card.png");

    public static final int BG_WIDTH = 64;
    public static final int BG_HEIGHT = 32;

    public static final int BUTTON_WIDTH = 64;
    public static final int BUTTON_HEIGHT = 16;

    private final Supplier<ItemStack> output;

    public RecipeOutput(int x, int y, int w, int h, Supplier<ItemStack> output) {
        super(x, y, w, h, Component.empty());
        this.output = output;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouse_x, int mouse_y, float delta)
    {
        RenderSystem.setShaderTexture(0, BG);

        if (isMouseOver(mouse_x, mouse_y))
        {
            graphics.blit(BG, getX(), getY(), 0, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT, BG_WIDTH, BG_HEIGHT);
        }
        else
        {
            graphics.blit(BG, getX(), getY(), 0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, BG_WIDTH, BG_HEIGHT);
        }


        ItemStack item = output.get();
        if (!item.isEmpty())
        {
            graphics.renderItem(item, getX(), getY());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrator) {}
}
