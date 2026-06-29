package com.auto_eating.gui;

import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiAutoEat extends GuiContainer {

    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
    private static final ResourceLocation FOOD_SLOT_TEXTURE = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");
    private static final ItemStack APPLE = new ItemStack(Items.APPLE);

    public GuiAutoEat(ContainerAutoEat container) {
        super(container);
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString("Auto-Eat", 8, 6, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // Draw food slot backgrounds
        mc.getTextureManager().bindTexture(FOOD_SLOT_TEXTURE);
        for (Slot slot : this.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                Gui.drawModalRectWithCustomSizedTexture(
                    guiLeft + slot.xPos + 1, guiTop + slot.yPos + 1,
                    0, 0, 16, 16, 16, 16);
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