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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerPlayer.class)
public abstract class ContainerPlayerMixin extends Container {

    private static final int AUTO_EATING_SLOT_X = 77;
    private static final int AUTO_EATING_SLOT_Y = 8;
    private static final int AUTO_EATING_SLOT_SPACING = 18;
    private static final int PLAYER_INVENTORY_START = 9;
    private static final int PLAYER_HOTBAR_END = 45;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void autoEating$addFoodSlots(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player, CallbackInfo ci) {
        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
        if (data == null) return;

        for (int i = 0; i < data.inventory.getSizeInventory(); i++) {
            this.addSlotToContainer(new SlotAutoFood(data.inventory, i, AUTO_EATING_SLOT_X, AUTO_EATING_SLOT_Y + i * AUTO_EATING_SLOT_SPACING));
        }
    }

    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true)
    private void autoEating$transferFoodToAutoSlots(EntityPlayer player, int index, CallbackInfoReturnable<ItemStack> cir) {
        Slot slot = index >= 0 && index < this.inventorySlots.size() ? this.inventorySlots.get(index) : null;
        if (slot == null || !slot.getHasStack()) return;

        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();

        if (slot instanceof SlotAutoFood) {
            if (!this.mergeItemStack(stack, 9, 45, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            cir.setReturnValue(original);
            return;
        }

        if (index >= PLAYER_INVENTORY_START && index < PLAYER_HOTBAR_END
            && stack.getItem() instanceof ItemFood
            && this.mergeIntoAutoEatingSlots(stack)) {
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            cir.setReturnValue(original);
        }
    }

    private boolean mergeIntoAutoEatingSlots(ItemStack stack) {
        boolean changed = false;

        for (int i = 0; i < this.inventorySlots.size() && !stack.isEmpty(); i++) {
            Slot slot = this.inventorySlots.get(i);
            if (!(slot instanceof SlotAutoFood)) continue;

            ItemStack existing = slot.getStack();

            if (!existing.isEmpty() && canStacksMerge(existing, stack)) {
                int limit = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                int movable = Math.min(stack.getCount(), limit - existing.getCount());

                if (movable > 0) {
                    existing.grow(movable);
                    stack.shrink(movable);
                    slot.onSlotChanged();
                    changed = true;
                }
            }
        }

        for (int i = 0; i < this.inventorySlots.size() && !stack.isEmpty(); i++) {
            Slot slot = this.inventorySlots.get(i);
            if (!(slot instanceof SlotAutoFood)) continue;

            if (!slot.getHasStack() && slot.isItemValid(stack)) {
                int movable = Math.min(stack.getCount(), Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize()));
                ItemStack inserted = stack.copy();
                inserted.setCount(movable);
                slot.putStack(inserted);
                stack.shrink(movable);
                changed = true;
            }
        }

        return changed;
    }

    private static boolean canStacksMerge(ItemStack first, ItemStack second) {
        return first.getItem() == second.getItem()
            && first.getMetadata() == second.getMetadata()
            && ItemStack.areItemStackTagsEqual(first, second);
    }
}
