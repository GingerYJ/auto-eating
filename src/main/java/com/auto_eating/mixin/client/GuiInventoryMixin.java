package com.auto_eating.mixin.client;

import com.auto_eating.gui.SlotAutoFood;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiInventory.class)
public abstract class GuiInventoryMixin extends InventoryEffectRenderer {

    private static final ResourceLocation AUTO_EATING_SLOT_TEX = new ResourceLocation("textures/gui/container/generic_54.png");
    private static final ResourceLocation AUTO_EATING_FOOD_SLOT_TEX = new ResourceLocation("auto_eating", "textures/gui/emptyslots/emptyslot_food.png");

    private GuiInventoryMixin() {
        super(null);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void autoEating$drawFoodSlots(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(AUTO_EATING_SLOT_TEX);
        for (Slot slot : this.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood) {
                Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + slot.xPos - 1, this.guiTop + slot.yPos - 1, 7, 17, 18, 18, 256, 256);
            }
        }

        this.mc.getTextureManager().bindTexture(AUTO_EATING_FOOD_SLOT_TEX);
        for (Slot slot : this.inventorySlots.inventorySlots) {
            if (slot instanceof SlotAutoFood && !slot.getHasStack()) {
                Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + slot.xPos + 1, this.guiTop + slot.yPos + 1, 0, 0, 16, 16, 16, 16);
            }
        }
    }
}
