package com.auto_eating.events;

import com.auto_eating.gui.SlotAutoFood;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class GuiEventHandler {

    private static final ResourceLocation FOOD_SLOT_TEXTURE =
        new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiInventory) {
            GuiInventory gui = (GuiInventory) event.getGui();
            Minecraft mc = Minecraft.getMinecraft();

            // Get guiLeft/guiTop via reflection
            int guiLeft = (mc.displayWidth - 176) / 2;
            int guiTop = (mc.displayHeight - 166) / 2;
            try {
                Field leftField = GuiContainer.class.getDeclaredField("guiLeft");
                leftField.setAccessible(true);
                guiLeft = leftField.getInt(gui);
                Field topField = GuiContainer.class.getDeclaredField("guiTop");
                topField.setAccessible(true);
                guiTop = topField.getInt(gui);
            } catch (Exception ignored) {}

            // Set up GL state for texture rendering
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            // Draw food icon for each SlotAutoFood
            mc.getTextureManager().bindTexture(FOOD_SLOT_TEXTURE);
            for (Slot slot : gui.inventorySlots.inventorySlots) {
                if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                    int x = guiLeft + slot.xPos + 1;
                    int y = guiTop + slot.yPos + 1;
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
                }
            }
        }
    }
}