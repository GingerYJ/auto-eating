package com.auto_eating.mixin;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import com.auto_eating.gui.SlotAutoFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.inventory.ContainerPlayer")
public abstract class MixinContainerPlayer extends Container {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(net.minecraft.entity.player.InventoryPlayer inv, boolean local, EntityPlayer player, CallbackInfo ci) {
        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
        if (data != null) {
            for (int i = 0; i < 3; i++) {
                this.addSlotToContainer(new SlotAutoFood(
                    data.inventory, i, 77, 8 + i * 18));
            }
        }
    }

    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true)
    private void onTransferStackInSlot(EntityPlayer player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (index >= 46 && index <= 48) {
            Slot slot = this.inventorySlots.get(index);
            if (slot != null && slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                ItemStack result = stack.copy();
                if (this.mergeItemStack(stack, 9, 45, false)) {
                    if (stack.isEmpty()) {
                        slot.putStack(ItemStack.EMPTY);
                    }
                    slot.onSlotChanged();
                    cir.setReturnValue(result);
                    cir.cancel();
                    return;
                }
            }
            cir.setReturnValue(ItemStack.EMPTY);
            cir.cancel();
            return;
        }

        if (index >= 9 && index < 46) {
            Slot slot = this.inventorySlots.get(index);
            if (slot != null && slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof ItemFood) {
                    ItemStack copy = stack.copy();
                    boolean moved = false;
                    for (int i = 46; i < 49; i++) {
                        Slot foodSlot = this.inventorySlots.get(i);
                        if (!foodSlot.getHasStack()) {
                            foodSlot.putStack(copy.splitStack(Math.min(copy.getCount(), 64)));
                            if (copy.isEmpty()) {
                                stack.setCount(0);
                                slot.putStack(ItemStack.EMPTY);
                            } else {
                                stack.setCount(copy.getCount());
                            }
                            slot.onSlotChanged();
                            foodSlot.onSlotChanged();
                            moved = true;
                            break;
                        }
                    }
                    if (moved) {
                        cir.setReturnValue(ItemStack.EMPTY);
                        cir.cancel();
                    }
                }
            }
        }
    }
}