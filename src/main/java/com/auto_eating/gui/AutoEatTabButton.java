package com.auto_eating.gui;

import com.auto_eating.AutoEatingMod;
import com.auto_eating.network.PacketOpenAutoEatGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AutoEatTabButton extends GuiButton {

    private static final ResourceLocation ICON = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    public AutoEatTabButton(int id, int x, int y) {
        super(id, x, y, 18, 18, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!visible) return;
        this.hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

        // Draw slot background
        mc.getTextureManager().bindTexture(ICON);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 18, 18, 18, 18);

        // Hover glow
        if (hovered) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.3F);
            drawRect(x, y, x + width, y + height, 0xFFFFFF);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            AutoEatingMod.network.sendToServer(new PacketOpenAutoEatGui());
            return true;
        }
        return false;
    }
}