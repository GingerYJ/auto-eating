package com.auto_eating.events;

import com.auto_eating.gui.SlotAutoFood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEventHandler {

    private static final ResourceLocation FOOD_SLOT_TEX = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    private static final int SLOT_BACKGROUND = 0xFF8B8B8B;
    private static final int SLOT_SHADOW = 0xFF373737;
    private static final int SLOT_HIGHLIGHT = 0xFFFFFFFF;

    @SubscribeEvent
    public void onDrawForeground(GuiContainerEvent.DrawForeground event) {
        if (!(event.getGuiContainer() instanceof GuiInventory)) return;

        GuiContainer gui = event.getGuiContainer();
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood) {
                if (!slot.getHasStack()) {
                    Gui.drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, SLOT_BACKGROUND);
                    mc.getTextureManager().bindTexture(FOOD_SLOT_TEX);
                    Gui.drawModalRectWithCustomSizedTexture(slot.xPos, slot.yPos, 0, 0, 16, 16, 16, 16);
                }

                drawSlotFrame(slot.xPos, slot.yPos);
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
    }

    private static void drawSlotFrame(int x, int y) {
        Gui.drawRect(x - 1, y - 1, x + 17, y, SLOT_SHADOW);
        Gui.drawRect(x - 1, y - 1, x, y + 17, SLOT_SHADOW);
        Gui.drawRect(x - 1, y + 16, x + 17, y + 17, SLOT_HIGHLIGHT);
        Gui.drawRect(x + 16, y - 1, x + 17, y + 17, SLOT_HIGHLIGHT);
    }
}
