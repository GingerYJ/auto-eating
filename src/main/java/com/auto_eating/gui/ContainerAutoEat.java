package com.auto_eating.gui;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ContainerAutoEat extends Container {

    public ContainerAutoEat(EntityPlayer player) {
        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
        if (data == null) return;

        // Only 3 food slots, centered
        for (int i = 0; i < 3; i++) {
            this.addSlotToContainer(new SlotAutoFood(data.inventory, i, 28 + i * 18, 22));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            ItemStack ret = stack.copy();
            if (stack.getItem() instanceof ItemFood) {
                // Food: take out to player inventory
                if (!this.mergeItemStack(stack, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
                if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
                slot.onSlotChanged();
                return ret;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}