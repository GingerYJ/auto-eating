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

    private static final ResourceLocation SLOT_TEX = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation FOOD_SLOT_TEX = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    @SubscribeEvent
    public void onDrawForeground(GuiContainerEvent.DrawForeground event) {
        if (!(event.getGuiContainer() instanceof GuiInventory)) return;

        GuiContainer gui = event.getGuiContainer();
        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);

        mc.getTextureManager().bindTexture(SLOT_TEX);
        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood) {
                Gui.drawModalRectWithCustomSizedTexture(slot.xPos - 1, slot.yPos - 1, 7, 17, 18, 18, 256, 256);
            }
        }

        mc.getTextureManager().bindTexture(FOOD_SLOT_TEX);
        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                Gui.drawModalRectWithCustomSizedTexture(slot.xPos + 1, slot.yPos + 1, 0, 0, 16, 16, 16, 16);
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
    }
}
