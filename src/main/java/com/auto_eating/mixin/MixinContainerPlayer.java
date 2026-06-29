package com.auto_eating.mixin;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import com.auto_eating.gui.SlotAutoFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerPlayer.class)
public abstract class MixinContainerPlayer extends Container {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(InventoryPlayer inv, boolean local, EntityPlayer player, CallbackInfo ci) {
        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
        if (data != null) {
            // 3 food slots in a vertical column, clearly separated from armor (x=8)
            for (int i = 0; i < 3; i++) {
                this.addSlotToContainer(new SlotAutoFood(
                    data.inventory, i, 77, 8 + i * 18));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            if (index >= 46 && index <= 48) {
                if (!this.mergeItemStack(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
                if (stack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                }
                slot.onSlotChanged();
                return itemstack;
            }

            if (stack.getItem() instanceof ItemFood) {
                if (!this.mergeItemStack(stack, 46, 49, false)) {
                    // fall through
                } else {
                    if (stack.isEmpty()) {
                        slot.putStack(ItemStack.EMPTY);
                    }
                    slot.onSlotChanged();
                    return itemstack;
                }
            }

            if (index < 9) {
                if (!this.mergeItemStack(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 36 && index < 45) {
                if (!this.mergeItemStack(stack, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 9 && index < 36) {
                if (!this.mergeItemStack(stack, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 45) {
                if (!this.mergeItemStack(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }
}