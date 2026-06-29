package com.auto_eating.gui;

import com.auto_eating.capability.AutoEatData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public final class AutoEatSlotManager {

    public static final int SLOT_X = 77;
    public static final int SLOT_Y = 8;
    public static final int SLOT_SPACING = 18;

    private AutoEatSlotManager() {}

    public static void install(EntityPlayer player, AutoEatData data) {
        if (player == null || data == null || player.inventoryContainer == null) return;

        Container container = player.inventoryContainer;
        for (int i = 0; i < data.inventory.getSizeInventory(); i++) {
            if (!hasSlot(container, data, i)) {
                addSlot(container, new SlotAutoFood(data.inventory, i, SLOT_X, SLOT_Y + i * SLOT_SPACING));
            }
        }
    }

    private static boolean hasSlot(Container container, AutoEatData data, int index) {
        for (Slot slot : container.inventorySlots) {
            if (slot instanceof SlotAutoFood && slot.inventory == data.inventory && slot.getSlotIndex() == index) {
                return true;
            }
        }
        return false;
    }

    private static void addSlot(Container container, Slot slot) {
        slot.slotNumber = container.inventorySlots.size();
        container.inventorySlots.add(slot);
        container.inventoryItemStacks.add(ItemStack.EMPTY);
    }
}
