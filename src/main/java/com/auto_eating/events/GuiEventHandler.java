package com.auto_eating.events;

import com.auto_eating.gui.AutoEatTabButton;
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

    private static final ResourceLocation FOOD_SLOT_TEX = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");
    private static final int TAB_ID = 12345;
    private static boolean buttonAdded = false;

    @SubscribeEvent
    public void onGuiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiInventory) {
            GuiInventory gui = (GuiInventory) event.getGui();
            int guiLeft = (Minecraft.getMinecraft().displayWidth - 176) / 2;
            int guiTop = (Minecraft.getMinecraft().displayHeight - 166) / 2;
            try {
                Field leftField = GuiContainer.class.getDeclaredField("guiLeft");
                leftField.setAccessible(true); guiLeft = leftField.getInt(gui);
                Field topField = GuiContainer.class.getDeclaredField("guiTop");
                topField.setAccessible(true); guiTop = topField.getInt(gui);
            } catch (Exception ignored) {}

            // Remove old, add new
            event.getButtonList().removeIf(b -> b.id == TAB_ID);
            event.getButtonList().add(new AutoEatTabButton(
                TAB_ID, guiLeft + 136, guiTop + 62));
        }
    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        // Draw food icons on empty food slots in auto-eat GUI
        if (event.getGui() instanceof GuiContainer &&
            !(event.getGui() instanceof GuiInventory)) {
            GuiContainer gui = (GuiContainer) event.getGui();
            Minecraft mc = Minecraft.getMinecraft();
            int guiLeft = 0, guiTop = 0;
            try {
                Field lf = GuiContainer.class.getDeclaredField("guiLeft");
                lf.setAccessible(true); guiLeft = lf.getInt(gui);
                Field tf = GuiContainer.class.getDeclaredField("guiTop");
                tf.setAccessible(true); guiTop = tf.getInt(gui);
            } catch (Exception ignored) {}

            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, 1F);
            mc.getTextureManager().bindTexture(FOOD_SLOT_TEX);
            for (Slot slot : gui.inventorySlots.inventorySlots) {
                if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                    Gui.drawModalRectWithCustomSizedTexture(
                        guiLeft + slot.xPos + 1, guiTop + slot.yPos + 1,
                        0, 0, 16, 16, 16, 16);
                }
            }
        }
    }
}