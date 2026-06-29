package com.auto_eating.gui;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ContainerAutoEat extends Container {

    // Slot indices:
    // 0-2:   Food slots
    // 3-29:  Player inventory (27 slots)
    // 30-38: Hotbar (9 slots)
    private static final int FOOD_START = 0;
    private static final int FOOD_END = 3;
    private static final int INV_START = 3;
    private static final int INV_END = 30;
    private static final int HOTBAR_START = 30;
    private static final int HOTBAR_END = 39;

    public ContainerAutoEat(EntityPlayer player) {
        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);

        if (data == null) return;

        // 3 food slots at x=77, y=8, 26, 44
        for (int i = 0; i < 3; i++) {
            this.addSlotToContainer(new SlotAutoFood(data.inventory, i, 77, 8 + i * 18));
        }

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(
                    new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            if (index < FOOD_END) {
                if (!this.mergeItemStack(stack, INV_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (stack.getItem() instanceof ItemFood) {
                    if (this.mergeItemStack(stack, FOOD_START, FOOD_END, false)) {
                        // moved to food slot
                    } else {
                        if (index < INV_END) {
                            if (!this.mergeItemStack(stack, HOTBAR_START, HOTBAR_END, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else {
                            if (!this.mergeItemStack(stack, INV_START, INV_END, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                } else {
                    if (index < INV_END) {
                        if (!this.mergeItemStack(stack, HOTBAR_START, HOTBAR_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!this.mergeItemStack(stack, INV_START, INV_END, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
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

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}