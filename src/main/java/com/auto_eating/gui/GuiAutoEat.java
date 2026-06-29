package com.auto_eating.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiAutoEat extends GuiContainer {

    private static final ResourceLocation CHEST_TEX = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation FOOD_SLOT_TEX = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    public GuiAutoEat(ContainerAutoEat container) {
        super(container);
        this.xSize = 86;
        this.ySize = 54;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString("Auto-Eat Food Slots", 4, 4, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(CHEST_TEX);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Draw food icons on empty slots
        mc.getTextureManager().bindTexture(FOOD_SLOT_TEX);
        for (Slot slot : this.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                this.drawTexturedModalRect(
                    guiLeft + slot.xPos + 1, guiTop + slot.yPos + 1,
                    0, 0, 16, 16);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}